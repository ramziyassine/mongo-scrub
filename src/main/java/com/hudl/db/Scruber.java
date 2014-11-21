package com.hudl.db;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.hudl.config.ScrubConfiguration;
import com.hudl.config.ScrubProperties;
import com.hudl.util.HashingService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 *
 */
@Singleton
public class Scruber {

    private static final String DICTIONARY_ALPHABET = "qwertyuiopasdfghjklzxcvbnm";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); //From http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/


    private final ScrubConfiguration configuration;
    private final HashingService hashingService;
    private final AtomicReference<String> HASHED_PASSWORD_REFERENCE = new AtomicReference<>(null);
    private final Random random;
    private Map<String, ScrubStrategy> strategiesMap = Maps.newConcurrentMap();


    @Inject
    public Scruber(ScrubConfiguration configuration, HashingService hashingService) {
        this.configuration = configuration;
        this.hashingService = hashingService;
        this.random = new Random();
    }

    public String scrub(String key, String value) {
        ScrubProperties properties = configuration.get();
        ScrubStrategy strategy = getStrategyForKey(key, properties);
        String updatedValue = scrub(strategy, key, value);
        return updatedValue;
    }

    private String scrub(ScrubStrategy strategy, String key, String value) {

        switch (strategy) {

            case NONE:
                return value;
            case NULL:
                return null;
            case PASSWORD:
                return getMaskedPassword();
            case MASK:
                return maskValue(value);
            case SHUFFLE:
                return shuffle(value);
            case SUBSTITUTION:
                return substitute(key, value);
            default:
                throw new IllegalArgumentException("Unplanned strategy, this error will occur if the utility somehow supported another strategy that has not been coded against.");
        }
    }

    private String substitute(String key, String value) {

        boolean isEmail = EMAIL_PATTERN.matcher(value).matches();
        if (isEmail) {
            String temp = value.substring(0, value.indexOf("@"));
            return generateWord(temp.length()) + "@testing.com";
        }

        return generateWord(value.length());
    }

    private String shuffle(String value) {

        List<String> characters = Lists.newArrayList();
        for (char character : value.toCharArray()) {
            characters.add(Character.toString(character));
        }
        Collections.shuffle(characters);
        return Joiner.on("").join(characters);
    }

    private String maskValue(String value) {

        char[] array = value.toCharArray();
        int length = array.length;
        char[] newArray = new char[length];

        boolean toMask = false;
        for (int i = 0; i < length; ++i) {

            int numberBetween1And10 = random.nextInt(10) + 1;
            int level = numberBetween1And10 % 2 ;
            if (level >= 1) {
                toMask = false;
            } else {
                toMask = true;
            }

            if (toMask) {
                newArray[i] = 'X';
            } else {
                newArray[i] = array[i];
            }
        }
        return new String(newArray);
    }


    private ScrubStrategy getStrategyForKey(String key, ScrubProperties properties) {

        ScrubStrategy strategy = strategiesMap.get(key);
        if (strategy != null) {
            return strategy;
        }

        boolean matchFound = false;
        String patternKeyUsed = null;
        Map<String, List<Pattern>> patterns = properties.getConfiguredPatterns();
        for (Map.Entry<String, List<Pattern>> patternsEntry : patterns.entrySet()) {
            String patternsKey = patternsEntry.getKey();
            List<Pattern> allPatterns = patternsEntry.getValue();
            for (Pattern pattern : allPatterns) {
                if (pattern.matcher(key).matches()) {
                    matchFound = true;
                    patternKeyUsed = patternsKey;
                    break;
                }
            }

            if (matchFound) {
                break;
            }
        }

        ScrubStrategy result = ScrubStrategy.NONE;

        if (matchFound) {
            result = properties.getConfiguredStrategies().get(patternKeyUsed);
        }

        strategiesMap.put(key, result);
        return result;
    }

    private String getMaskedPassword() {

        String result = HASHED_PASSWORD_REFERENCE.get();
        if (result == null) {
            String hashedPassword = hashingService.hash("Testing123!");
            if (HASHED_PASSWORD_REFERENCE.compareAndSet(null, hashedPassword)) {
                return hashedPassword;
            }
        }

        return HASHED_PASSWORD_REFERENCE.get();
    }


    private String generateWord(int length) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(DICTIONARY_ALPHABET.length());
            char ch = DICTIONARY_ALPHABET.charAt(number);
            builder.append(ch);
        }

        return builder.toString();
    }
}