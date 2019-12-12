package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Appliance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.ApplianceKey;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ApplianceRepository extends ReactiveCrudRepository<Appliance, ApplianceKey> {

    Flux<Appliance> findByKeyHouseholdId(final UUID householdId);

    Mono<Appliance> findByKeyHouseholdIdAndKeyId(final UUID householdId, final UUID id);
}
