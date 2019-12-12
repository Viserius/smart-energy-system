package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table
public class Generator implements Serializable {

    @PrimaryKey
    private final GeneratorKey key;

    @Column
    private final String name;

    @Column
    private final float currentPowerProduction;

    @Column
    private final float averagePowerProduction;

    @Column
    private final boolean enabled;

    public Generator(
        final GeneratorKey key,
        final String name,
        final float currentPowerProduction,
        final float averagePowerProduction,
        final boolean enabled
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
