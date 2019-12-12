package org.rug.wacc2019smartenergy.deviceactivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Generator implements Serializable {

    private final GeneratorKey key;

    private final String name;

    private final float currentPowerProduction;

    private final float averagePowerProduction;

    private final boolean enabled;

    @JsonCreator
    public Generator(
        @JsonProperty("key") final GeneratorKey key,
        @JsonProperty("name") final String name,
        @JsonProperty("currentPowerProduction") final float currentPowerProduction,
        @JsonProperty("averagePowerProduction") final float averagePowerProduction,
        @JsonProperty("enabled") final boolean enabled
    ) {
        this.key = key;
        this.name = name;
        this.currentPowerProduction = currentPowerProduction;
        this.averagePowerProduction = averagePowerProduction;
        this.enabled = enabled;
    }

    public GeneratorKey getKey() {
        return key;
    }

    public float getCurrentPowerProduction() {
        return currentPowerProduction;
    }

    public float getAveragePowerProduction() {
        return averagePowerProduction;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }
}
