package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Battery;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.BatteryKey;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BatteryRepository extends ReactiveCrudRepository<Battery, BatteryKey> {

    Flux<Battery> findByKeyHouseholdId(final UUID householdId);

    Mono<Battery> findByKeyHouseholdIdAndKeyId(final UUID householdId, final UUID id);

}
