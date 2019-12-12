package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdEnergyProduction;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdEnergyProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
public class HouseholdEnergyProductionController {

    private HouseholdEnergyProductionRepository householdEnergyProductionRepository;

    @Autowired
    public HouseholdEnergyProductionController(
            HouseholdEnergyProductionRepository householdEnergyProductionRepository
    ) {
        this.householdEnergyProductionRepository = householdEnergyProductionRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/energy_productions")
    public Flux<HouseholdEnergyProduction> energyProductions() {
        return householdEnergyProductionRepository.findAll();
    }

    @RequestMapping(method=GET, value="/households/{uuid}/energy_productions/{timestamp}")
    public Flux<HouseholdEnergyProduction> energyProductionsForHouseholdAfterTimestamp(
            @PathVariable UUID uuid,
            @PathVariable long timestamp
    ) {
        Instant ts = Instant.ofEpochMilli(timestamp);
        LocalDate date = LocalDate.fromMillisSinceEpoch(timestamp);
        return householdEnergyProductionRepository.findByHouseholdIdAndDateAndTimestampAfter(uuid, date, ts);
    }
}
