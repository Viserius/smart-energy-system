package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import com.datastax.driver.core.LocalDate;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table
public class HouseholdBalance {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private UUID householdId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    private LocalDate date;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 3, ordering = Ordering.DESCENDING)
    private Instant timestamp;

    private float value;

    public HouseholdBalance() {
    }

    public HouseholdBalance(
            final UUID householdId,
            final LocalDate date,
            final Instant timestamp,
            final float value
    ) {
        this.householdId = householdId;
        this.date = date;
        this.timestamp = timestamp;
        this.value = value;
    }

    public UUID getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(UUID householdId) {
        this.householdId = householdId;
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
