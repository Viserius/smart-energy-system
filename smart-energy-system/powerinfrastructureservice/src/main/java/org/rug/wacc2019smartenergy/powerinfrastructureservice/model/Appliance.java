package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table
public class Appliance implements Serializable {

    @PrimaryKey
    private final ApplianceKey key;

    @Column
    private final String name;

    @Column
    private final float currentPowerUsage;

    @Column
    private final float averagePowerUsage;

    @Column
    private final boolean enabled;

    public Appliance(
        final ApplianceKey key,
        final String name,
        final float currentPowerUsage,
        final float averagePowerUsage,
        final boolean enabled
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
