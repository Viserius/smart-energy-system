package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table
public class Battery implements Serializable {

    @PrimaryKey
    private final BatteryKey key;

    @Column
    private final String name;

    @Column
    private double currentPowerStorage;

    @Column
    private final float totalPowerStorage;

    @Column
    private boolean enabled;

    public Battery(
            final BatteryKey key,
            final String name,
            double currentPowerStorage,
            final float totalPowerStorage,
            boolean enabled
    ) {
        this.key = key;
        this.name = name;
        this.currentPowerStorage = currentPowerStorage;
        this.totalPowerStorage = totalPowerStorage;
        this.enabled = enabled;
    }

    public BatteryKey getKey() {
        return key;
    }

    public double getCurrentPowerStorage() {
        return currentPowerStorage;
    }

    public float getTotalPowerStorage() {
        return totalPowerStorage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public void setCurrentPowerStorage(double currentPowerStorage) {
        this.currentPowerStorage = currentPowerStorage;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
