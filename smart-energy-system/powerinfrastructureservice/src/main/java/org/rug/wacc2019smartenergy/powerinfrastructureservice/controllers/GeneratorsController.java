package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Generator;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.GeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
public class GeneratorsController {

    private GeneratorRepository generatorRepository;

    @Autowired
    public GeneratorsController(GeneratorRepository generatorRepository) {
        this.generatorRepository = generatorRepository;
    }

    @RequestMapping(method=GET, value="/households/{uuid}/generators")
    public Flux<Generator> generators(@PathVariable UUID uuid) {
        return generatorRepository.findByKeyHouseholdId(uuid);
    }

    @RequestMapping(method=GET, value="/households/{uuid}/generators/{id}")
    public Mono<Generator> generator(
            @PathVariable
            @Min(value = 0, message = "id must be non-negative.") // to check if in browser or exception
                    UUID id,
            @PathVariable UUID uuid
    ) {
        return this.generatorRepository.findByKeyHouseholdIdAndKeyId(uuid, id);
    }

    @PutMapping(value = "/households/{uuid}/generators/{id}")
    public Mono<Generator> put(@PathVariable UUID uuid,
                             @PathVariable UUID id,
                             @RequestBody Generator generator) {
        return generatorRepository.save(generator);
    }
}
