package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import com.datastax.driver.core.LocalDate;
import com.google.common.util.concurrent.AtomicDouble;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Household;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdBalance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdBalanceRepository;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HouseholdsController {

    private HouseholdRepository householdRepository;
    private HouseholdBalanceRepository householdBalanceRepository;
    private BatteriesController batteriesController;

    @Value("${powerprocessing.minimum_energy_unit}")
    private double MINIMUM_ENERGY_UNIT=1E-7;

    @Autowired
    public HouseholdsController(
            HouseholdRepository householdRepository,
            HouseholdBalanceRepository householdBalanceRepository,
            BatteriesController batteriesController
    ) {
        this.householdRepository = householdRepository;
        this.householdBalanceRepository = householdBalanceRepository;
        this.batteriesController = batteriesController;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/households")
    public Flux<Household> households() {
        return householdRepository.findAll();
    }

    @CrossOrigin
    @RequestMapping(method=GET, value="/households/{id}")
    public Mono<Household> get(
            @PathVariable
            @Min(value = 0, message = "id must be non-negative.") // to check if in browser or exception
                    UUID id
    ) {
        return this.householdRepository.findById(id);
    }

    @CrossOrigin
    @PutMapping(value = "/households/{id}")
    public Mono<Household> put(@PathVariable UUID id,
                                    @RequestBody Household household) {
        return householdRepository.save(household);
    }

    /**
     * A household makes a purchase for a specified amount of energy.
     * @param householdId Household that purchases energy
     * @param amountToPurchase Energy in kWh
     */
    public void purchaseEnergy(UUID householdId, Instant timestamp, double amountToPurchase) {
        Mono<Household> householdMono = householdRepository.findById(householdId);

        // Check if the amount to buy is at least the minimum units
        if(amountToPurchase < MINIMUM_ENERGY_UNIT) {
            System.out.println("Energy is not minimum energy unit... Free!");
            householdMono.subscribe(
                    household -> this.saveHouseholdBalance(
                            householdId,
                            timestamp,
                            household.getMyTotalEnergyCosts().floatValue()
                    )
            );
            return;
        }

        AtomicDouble amountToPurchaseAtomic = new AtomicDouble(amountToPurchase);
        householdMono.subscribe(household -> {
            // If trading it enabled, purchase as much as possible from traders at the lowest price
            System.out.println("Household: " + household.getId() + " is now purchasing " + amountToPurchase);
            if(household.getIsTrading()) {
                double purchasedFromTraders = purchaseEnergyFromTraders(amountToPurchaseAtomic.get(), household);
                amountToPurchaseAtomic.set(amountToPurchaseAtomic.get() - purchasedFromTraders);
                System.out.println("Household: " + household.getId() + " purchased: " + purchasedFromTraders + " from traders.");
            }

            // Purchase (the remainder) from the energy provider
            System.out.println("Household: " + household.getId() + " purchased the remainding: " + amountToPurchaseAtomic.get() + " from provider.");
            purchaseEnergyFromProvider(amountToPurchaseAtomic.get(), household);

            this.saveHouseholdBalance(householdId, timestamp, household.getMyTotalEnergyCosts().floatValue());
            System.out.println("Saved Household " + household.getId() + " after purchasing...");
        });
    }

    /**
     * Purchase a specific amount of energy from the household's provider
     * @param amountToPurchase Energy in kWh
     * @param household The purchasing household
     */
    private void purchaseEnergyFromProvider(double amountToPurchase, Household household) {
        BigDecimal providerCosts = household.getMyEnergyBuyPriceFromProvider()
                .multiply(BigDecimal.valueOf(amountToPurchase));
        household.setMyTotalEnergyCosts(household.getMyTotalEnergyCosts().add(providerCosts));

        // Persistence
        this.householdRepository.save(household).subscribe();
        System.out.println("Purchased Energy from provider: " + amountToPurchase + " for the price: " + providerCosts.toString());
    }

    /**
     * Purchase a specific amount of energy from traders.
     * If there is not enough supply from the traders, the buyer will simply buy nothing.
     * @param amountToPurchase Energy in kWh
     * @param buyer Buying household
     * @return The amount of energy that was purchased from traders
     */
    private double purchaseEnergyFromTraders(double amountToPurchase, Household buyer) {
        AtomicDouble amountToPurchaseAtomic = new AtomicDouble(amountToPurchase);
        Flux<Household> householdFlux = this.householdRepository
                .findByIsTradingIsTrueAndMyEnergyTradingSellPriceLessThanEqualOrderByMyEnergyTradingSellPrice(
                        buyer.getMyEnergyBuyPriceFromProvider()
                );
        System.out.println("Household: " + buyer.getId() + " tries to buy " + amountToPurchase + " from traders.");
        householdFlux.doOnNext(tradingHousehold -> {
            // For each household, try to draw as much power as possible
            double energyPurchased = this.batteriesController.drawPowerFromHouseholdBattery(
                    tradingHousehold.getId(),
                    amountToPurchaseAtomic.get(),
                    true);
            amountToPurchaseAtomic.set(amountToPurchaseAtomic.get() - energyPurchased);

            // Transfer money from the purchasing household to the delivering household
            BigDecimal price = tradingHousehold.getMyEnergyTradingSellPrice().multiply(BigDecimal.valueOf(energyPurchased));
            buyer.setMyTotalEnergyCosts(buyer.getMyTotalEnergyCosts().add(price));
            tradingHousehold.setMyTotalEnergyCosts(tradingHousehold.getMyTotalEnergyCosts().subtract(price));

            // Persist
            this.householdRepository.save(buyer).subscribe();
            this.householdRepository.save(tradingHousehold).subscribe();
            System.out.println("Purchased Energy from traders: " + energyPurchased + " for the price: " + price.toString());
        }).blockLast();
        return amountToPurchaseAtomic.get();
    }

    /**
     * If trading is enabled, traders can tap energy directly from the batteries of the household.
     * However, if a household's battery is full, and the household still produces energy,
     *   this surplus gets sold to the provider.
     * @param householdId The household
     * @param amountToSell How much kWh to sell to the provider
     */
    public void sellEnergy(UUID householdId, Instant timestamp, double amountToSell) {
        Mono<Household> householdMono = householdRepository.findById(householdId);

        // Check if the amount to sell is at least the minimum units
        if(amountToSell < MINIMUM_ENERGY_UNIT) {
            householdMono.subscribe(
                    household -> this.saveHouseholdBalance(
                            householdId,
                            timestamp,
                            household.getMyTotalEnergyCosts().floatValue()
                    )
            );
            return;
        }

        // Sell the surplus to the energy provider
        householdMono.subscribe(household -> {

            // Compute new costs
            BigDecimal revenue = household.getMyEnergySellPriceToProvider().multiply(BigDecimal.valueOf(amountToSell));
            BigDecimal newProviderCosts = household.getMyTotalEnergyCosts().subtract(revenue);

            // Persist
            household.setMyTotalEnergyCosts(newProviderCosts);
            this.householdRepository.save(household).subscribe();
            System.out.println("Energy sold to the provider: " + amountToSell + " for the price of: " + revenue.toString());

            this.saveHouseholdBalance(householdId, timestamp, household.getMyTotalEnergyCosts().floatValue());
        });
    }

    private void saveHouseholdBalance(UUID householdId, Instant timestamp, float value) {
        householdBalanceRepository.save(new HouseholdBalance(
                householdId,
                LocalDate.fromMillisSinceEpoch(timestamp.toEpochMilli()),
                timestamp,
                value
        )).subscribe();
    }
}
