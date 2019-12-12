package org.rug.wacc2019smartenergy.deviceactivity.model;

import java.util.UUID;

public class Household {
    private UUID id;
    private String name;
    private boolean isTrading;

    public Household(){
    }

    public Household(UUID id, String name, boolean isTrading) {
        this.id = id;
        this.name = name;
        this.isTrading = isTrading;
    }

    public Household(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTrading() {
        return isTrading;
    }

    public void setTrading(boolean trading) {
        isTrading = trading;
    }
}
