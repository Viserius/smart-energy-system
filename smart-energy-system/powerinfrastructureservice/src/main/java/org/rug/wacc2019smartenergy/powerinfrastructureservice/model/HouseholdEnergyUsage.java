package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import com.datastax.driver.core.LocalDate;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Table
public class HouseholdEnergyUsage {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private UUID householdId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    private LocalDate date;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 3, ordering = Ordering.ASCENDING)
    private Instant timestamp;

    private double value;

    public HouseholdEnergyUsage() {
    }

    public HouseholdEnergyUsage(
            final UUID householdId,
            final LocalDate date,
            final Instant timestamp,
            final double value
    ) {
        this.householdId = householdId;
        this.date = date;
        this.timestamp = timestamp;
        this.value = value;
    }

    public HouseholdEnergyUsage(
            final UUID householdId,
            final float value
    ) {
        this.householdId = householdId;
        this.date = LocalDate.fromMillisSinceEpoch(new Date().getTime());
        this.timestamp = Instant.now();
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UUID getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(UUID householdId) {
        this.householdId = householdId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
