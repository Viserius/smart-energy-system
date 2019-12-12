package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Appliance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.ApplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AppliancesController {

    private ApplianceRepository applianceRepository;

    @Autowired
    public AppliancesController(ApplianceRepository applianceRepository) {
        this.applianceRepository = applianceRepository;
    }

    @CrossOrigin
    @RequestMapping(method=GET, value="/households/{uuid}/appliances")
    public Flux<Appliance> appliances(
            @PathVariable UUID uuid
    ) {
        return applianceRepository.findByKeyHouseholdId(uuid);
    }

    @CrossOrigin
    @RequestMapping(method=GET, value="/households/{uuid}/appliances/{id}")
    public Mono<Appliance> appliance(
            @PathVariable UUID uuid,
            @PathVariable
            @Min(value = 0, message = "id must be non-negative.") // to check if in browser or exception
        UUID id
    ) {
        return this.applianceRepository.findByKeyHouseholdIdAndKeyId(uuid, id);
    }

    @CrossOrigin
    @PutMapping(value = "/households/{uuid}/appliances/{id}")
    public Mono<Appliance> put(@PathVariable UUID uuid,
                               @PathVariable UUID id,
                               @RequestBody Appliance appliance) {
        return applianceRepository.save(appliance);
    }
}
