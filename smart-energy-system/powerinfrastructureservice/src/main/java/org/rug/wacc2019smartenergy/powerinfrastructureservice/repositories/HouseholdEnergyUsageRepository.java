package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdEnergyUsage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

public interface HouseholdEnergyUsageRepository extends ReactiveCrudRepository<HouseholdEnergyUsage, UUID> {

    Flux<HouseholdEnergyUsage> findByHouseholdId(final UUID householdId);

    Flux<HouseholdEnergyUsage> findByHouseholdIdAndDate(final UUID householdId, final LocalDate date);

    Flux<HouseholdEnergyUsage> findByHouseholdIdAndDateAndTimestampAfter(
            final UUID householdId,
            final LocalDate date,
            final Instant timestamp
    );
}
