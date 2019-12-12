package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdEnergyProduction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

public interface HouseholdEnergyProductionRepository extends ReactiveCrudRepository<HouseholdEnergyProduction, UUID> {

    Flux<HouseholdEnergyProduction> findByHouseholdId(final UUID householdId);

    Flux<HouseholdEnergyProduction> findByHouseholdIdAndDate(final UUID householdId, final LocalDate date);

    Flux<HouseholdEnergyProduction> findByHouseholdIdAndDateAndTimestampAfter(
            final UUID householdId,
            final LocalDate date,
            final Instant timestamp
    );
}
