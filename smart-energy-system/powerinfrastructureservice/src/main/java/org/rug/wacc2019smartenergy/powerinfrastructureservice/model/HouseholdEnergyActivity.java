package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import com.datastax.driver.core.LocalDate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class HouseholdEnergyActivity {

    private UUID householdId;

    private LocalDate date;

    private Instant timestamp;

    private double energyUsage;

    private double energyProduction;

    @Value("${powerprocessing.broadcast_interval_in_seconds}")
    private int powerStatisticBroadcastIntervalInSeconds = 30;

    @JsonCreator
    public HouseholdEnergyActivity(
            @JsonProperty("householdId") final UUID householdId,
            @JsonProperty("date") final Date date,
            @JsonProperty("timestamp") final Long timestamp,
            @JsonProperty("energyUsage") final float energyUsage,
            @JsonProperty("energyProduction") final float energyProduction) {
        this.householdId = householdId;
        this.date = LocalDate.fromMillisSinceEpoch(date.getTime());
        this.timestamp = Instant.ofEpochMilli(timestamp);
        this.energyUsage = energyUsage;
        this.energyProduction = energyProduction;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(UUID householdId) {
        this.householdId = householdId;
    }

    public double getEnergyUsage() {
        return energyUsage / 1000.0 / (60.0 * 60);
    }

    public void setEnergyUsage(double energyUsage) {
        this.energyUsage = energyUsage;
    }

    public double getEnergyProduction() {
        return energyProduction / 1000.0 / (60.0 * 60);
    }

    public void setEnergyProduction(double energyProduction) {
        this.energyProduction = energyProduction;
    }

    // TODO: In case connectivity is offline, re-broadcast net usage later.
    public double getNetEnergyUsage() {
        return this.getEnergyUsage() - this.getEnergyProduction();
    }
}
