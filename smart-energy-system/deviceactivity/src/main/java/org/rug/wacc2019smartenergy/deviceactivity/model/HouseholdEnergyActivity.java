package org.rug.wacc2019smartenergy.deviceactivity.model;


import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class HouseholdEnergyActivity {

    private UUID householdId;

    private Date date;

    private Long timestamp;

    private float energyUsage;

    private float energyProduction;


    public HouseholdEnergyActivity(final UUID householdId, final float energyUsage, final float energyProduction) {
        this.householdId = householdId;
        this.date = new Date();
        this.timestamp = Instant.now().toEpochMilli();
        this.energyUsage = energyUsage;
        this.energyProduction = energyProduction;
    }

    public UUID getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(UUID householdId) {
        this.householdId = householdId;
    }

    public long getDate() {
        return date.getTime();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp.toEpochMilli();
    }

    public float getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(float energyUsage) {
        this.energyUsage = energyUsage;
    }

    public float getEnergyProduction() {
        return energyProduction;
    }

    public void setEnergyProduction(float energyProduction) {
        this.energyProduction = energyProduction;
    }
}
