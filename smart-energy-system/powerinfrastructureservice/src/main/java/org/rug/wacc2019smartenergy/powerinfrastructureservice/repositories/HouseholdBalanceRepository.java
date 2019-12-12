package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdBalance;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface HouseholdBalanceRepository extends ReactiveCrudRepository<HouseholdBalance, UUID> {

    Flux<HouseholdBalance> findByHouseholdIdAndDateAndTimestampAfter(
            final UUID householdId,
            final LocalDate date,
            final Instant timestamp
    );

    Mono<HouseholdBalance> findFirstByHouseholdIdAndDate(
            final UUID householdId,
            final LocalDate date
    );
}
