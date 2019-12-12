package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Document
public class Household {
    @Id
    private UUID id;
    private String name;

    @Indexed
    private boolean isTrading;

    @Indexed
    private BigDecimal myEnergyTradingSellPrice; // per kWu
    private BigDecimal myEnergyBuyPriceFromProvider; // per kWu
    private BigDecimal myEnergySellPriceToProvider; // per kWu
    private BigDecimal myTotalEnergyCosts;

    public Household(){
    }

    public Household(UUID id, String name,
                     boolean isTrading,
                     BigDecimal myEnergyTradingSellPrice,
                     BigDecimal myEnergyBuyPriceFromProvider,
                     BigDecimal myEnergySellPriceToProvider,
                     BigDecimal myTotalEnergyCosts) {
        this.id = id;
        this.name = name;
        this.isTrading = isTrading;
        this.myEnergyTradingSellPrice = myEnergyTradingSellPrice;
        this.myEnergyBuyPriceFromProvider = myEnergyBuyPriceFromProvider;
        this.myTotalEnergyCosts = myTotalEnergyCosts;
        this.myEnergySellPriceToProvider = myEnergySellPriceToProvider;
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

    public boolean getIsTrading() {
        return isTrading;
    }

    public void setIsTrading(boolean trading) {
        isTrading = trading;
    }

    public BigDecimal getMyEnergyTradingSellPrice() {
        return myEnergyTradingSellPrice;
    }

    public void setMyEnergyTradingSellPrice(BigDecimal myEnergyTradingSellPrice) {
        this.myEnergyTradingSellPrice = myEnergyTradingSellPrice;
    }

    public BigDecimal getMyEnergyBuyPriceFromProvider() {
        return myEnergyBuyPriceFromProvider;
    }

    public void setMyEnergyBuyPriceFromProvider(BigDecimal myEnergyBuyPriceFromProvider) {
        this.myEnergyBuyPriceFromProvider = myEnergyBuyPriceFromProvider;
    }

    public BigDecimal getMyTotalEnergyCosts() {
        return myTotalEnergyCosts;
    }

    public BigDecimal getMyEnergySellPriceToProvider() {
        return myEnergySellPriceToProvider;
    }

    public void setMyEnergySellPriceToProvider(BigDecimal myEnergySellPriceToProvider) {
        this.myEnergySellPriceToProvider = myEnergySellPriceToProvider;
    }

    public void setMyTotalEnergyCosts(BigDecimal myTotalEnergyCosts) {
        this.myTotalEnergyCosts = myTotalEnergyCosts;
    }
}
