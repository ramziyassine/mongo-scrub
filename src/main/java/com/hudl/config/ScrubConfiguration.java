package com.hudl.config;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.*;
import com.google.common.io.Resources;
import com.google.inject.Singleton;
import com.hudl.db.ScrubStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the utility that will retrieve scrub properties.
 */
@Singleton
public class ScrubConfiguration {

    private static final Splitter SPLITTER_COMMA = Splitter.on(",").omitEmptyStrings().trimResults();
    private static final Pattern PATTERN_STRATEGIES = Pattern.compile("(scrub\\.)(.*)(\\.strategy)");
    private final AtomicReference<ScrubProperties> propertiesReference = new AtomicReference<>(null);

    /**
     * @return
     */
    public ScrubProperties get() {
        start();
        return propertiesReference.get();
    }


    /**
     * @return
     */
    private void start() {

        ScrubProperties scrubProperties = propertiesReference.get();
        if (scrubProperties != null) {
            return;
        }

        URL url = findFile("scrub.properties");
        Properties properties = getProperties(url);
        scrubProperties = toScrubProperties(properties);
        propertiesReference.set(scrubProperties);
    }

    /**
     * @param properties
     * @return
     */
    private ScrubProperties toScrubProperties(Properties properties) {

        Map<String, List<Pattern>> configuredPatterns = null;
        Map<String, ScrubStrategy> configuredStrategies = null;

        for (Enumeration<String> propertyNames = (Enumeration<String>) properties.propertyNames(); propertyNames.hasMoreElements(); ) {

            String key = propertyNames.nextElement();
            String patternsString = properties.getProperty(key);

            if (key.startsWith("scrub.patterns.")) {
                String patternKey = key.substring("scrub.patterns.".length());
                String patternValue = properties.getProperty(key);
                Iterable<String> patterns = SPLITTER_COMMA.split(patternValue);
                List<Pattern> regex = FluentIterable.from(patterns)//
                        .transform((String pattern) -> {

                            try {
                                return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                            } catch (Exception e) {
                                throw new RuntimeException("Illegal regex pattern: " + pattern + " defined @" + patternKey
                                        , e);
                            }
                        }) //
                        .toList();

                if (configuredPatterns == null) {
                    configuredPatterns = Maps.newHashMap();
                }
                configuredPatterns.put(patternKey, regex);
            }

            Matcher strategyMatch = PATTERN_STRATEGIES.matcher(key);

            if (strategyMatch.matches()) {
                String patternKey = strategyMatch.group(2);
                String strategyString = properties.getProperty(key);
                ScrubStrategy strategy = Enum.valueOf(ScrubStrategy.class, strategyString);
                if (configuredStrategies == null) {
                    configuredStrategies = Maps.newHashMap();
                }
                configuredStrategies.put(patternKey, strategy      );
            }
        }

        ScrubProperties result = new ScrubProperties();
        result.setConfiguredStrategies(configuredStrategies);
        result.setConfiguredPatterns(configuredPatterns);

        return result;
    }

    private Properties getProperties(URL url) {

        InputStream stream = null;
        try {
            Properties properties = new Properties();
            stream = url.openStream();
            properties.load(stream);
            return properties;

        } catch (Exception e) {
            throw Throwables.propagate(e);

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    //silently fail...
                }

            }
        }
    }


    private URL findFile(String fileName) {
        return Resources.getResource(fileName);
    }
}
