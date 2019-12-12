package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Generator;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.GeneratorKey;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GeneratorRepository extends ReactiveCrudRepository<Generator, GeneratorKey> {

    Flux<Generator> findByKeyHouseholdId(final UUID householdId);

    Mono<Generator> findByKeyHouseholdIdAndKeyId(final UUID householdId, final UUID id);
}
