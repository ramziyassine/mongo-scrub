package com.hudl.config;

import com.hudl.db.ScrubStrategy;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents the configuration that will guide the utility in scrubbing data.
 */
public class ScrubProperties {

    private Map<String, ScrubStrategy> configuredStrategies;
    private Map<String, List<Pattern>> configuredPatterns;

    public Map<String, ScrubStrategy> getConfiguredStrategies() {
        return configuredStrategies;
    }

    public void setConfiguredStrategies(Map<String, ScrubStrategy> configuredStrategies) {
        this.configuredStrategies = configuredStrategies;
    }

    public Map<String, List<Pattern>> getConfiguredPatterns() {
        return configuredPatterns;
    }

    public void setConfiguredPatterns(Map<String, List<Pattern>> configuredPatterns) {
        this.configuredPatterns = configuredPatterns;
    }
}
