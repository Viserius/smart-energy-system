package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import com.datastax.driver.core.utils.UUIDs;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class BatteryKey implements Serializable {

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    private UUID id;

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private UUID householdId;

    public BatteryKey() {
    }

    public BatteryKey(final UUID householdId) {
        this.id = UUIDs.timeBased();
        this.householdId = householdId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getHouseholdId() {
        return householdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplianceKey that = (ApplianceKey) o;
        return getId().equals(that.getId()) &&
                getHouseholdId().equals(that.getHouseholdId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHouseholdId());
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setHouseholdId(UUID householdId) {
        this.householdId = householdId;
    }
}
