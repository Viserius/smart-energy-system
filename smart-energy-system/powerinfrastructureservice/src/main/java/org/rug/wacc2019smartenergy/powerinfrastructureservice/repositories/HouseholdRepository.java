package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;


import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Household;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.UUID;

public interface HouseholdRepository extends ReactiveCrudRepository<Household, UUID> {

    // Find all households that have trading enabled, for which the price is less than the specified amount,
    //   sorted from lowest price to highest
    Flux<Household> findByIsTradingIsTrueAndMyEnergyTradingSellPriceLessThanEqualOrderByMyEnergyTradingSellPrice(BigDecimal maximumPrice);
}
