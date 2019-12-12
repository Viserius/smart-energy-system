package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdEnergyUsage;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdEnergyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@CrossOrigin
@RestController
public class HouseholdEnergyUsageController {

    private HouseholdEnergyUsageRepository householdEnergyUsageRepository;

    @Autowired
    public HouseholdEnergyUsageController(
            HouseholdEnergyUsageRepository householdEnergyUsageRepository
    ) {
        this.householdEnergyUsageRepository = householdEnergyUsageRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/energy_usages")
    public Flux<HouseholdEnergyUsage> energyUsages() {
        return householdEnergyUsageRepository.findAll();
    }

    @RequestMapping(method=GET, value="/households/{uuid}/energy_usages/{timestamp}")
    public Flux<HouseholdEnergyUsage> energyUsagesForHouseholdAfterTimestamp(
            @PathVariable UUID uuid,
            @PathVariable long timestamp
    ) {
        Instant ts = Instant.ofEpochMilli(timestamp);
        LocalDate date = LocalDate.fromMillisSinceEpoch(timestamp);
        return householdEnergyUsageRepository.findByHouseholdIdAndDateAndTimestampAfter(uuid, date, ts);
    }
}
