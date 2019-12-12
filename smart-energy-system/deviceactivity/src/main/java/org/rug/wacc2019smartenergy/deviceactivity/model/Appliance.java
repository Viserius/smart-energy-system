package org.rug.wacc2019smartenergy.deviceactivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Appliance implements Serializable {

    private final ApplianceKey key;

    private final String name;

    private final float currentPowerUsage;

    private final float averagePowerUsage;

    private final boolean enabled;

    @JsonCreator
    public Appliance(
        @JsonProperty("key") final ApplianceKey key,
        @JsonProperty("name") final String name,
        @JsonProperty("currentPowerUsage") final float currentPowerUsage,
        @JsonProperty("averagePowerUsage") final float averagePowerUsage,
        @JsonProperty("enabled") final boolean enabled
    ) {
        this.key = key;
        this.name = name;
        this.currentPowerUsage = currentPowerUsage;
        this.averagePowerUsage = averagePowerUsage;
        this.enabled = enabled;
    }

    public ApplianceKey getKey() {
        return key;
    }

    public float getCurrentPowerUsage() {
        return currentPowerUsage;
    }

    public float getAveragePowerUsage() {
        return averagePowerUsage;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
