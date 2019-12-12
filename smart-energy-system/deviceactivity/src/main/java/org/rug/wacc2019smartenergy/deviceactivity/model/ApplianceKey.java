package org.rug.wacc2019smartenergy.deviceactivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ApplianceKey implements Serializable {

    private UUID id;

    private UUID householdId;

    @JsonCreator
    public ApplianceKey(
            @JsonProperty("id") final UUID id,
            @JsonProperty("householdId") final UUID householdId
    ) {
        this.id = id;
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
}
