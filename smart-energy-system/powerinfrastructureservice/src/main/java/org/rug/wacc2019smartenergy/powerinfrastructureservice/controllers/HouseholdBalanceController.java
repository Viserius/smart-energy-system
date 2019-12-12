package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdBalance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@CrossOrigin
@RestController
public class HouseholdBalanceController {

    private HouseholdBalanceRepository householdBalanceRepository;

    @Autowired
    public HouseholdBalanceController(
            HouseholdBalanceRepository householdBalanceRepository
    ) {
        this.householdBalanceRepository = householdBalanceRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/balances")
    public Flux<HouseholdBalance> balances() {
        return householdBalanceRepository.findAll();
    }

    @RequestMapping(method=GET, value="/households/{uuid}/balances/{timestamp}")
    public Flux<HouseholdBalance> balancesForHouseholdAfterTimestamp(
            @PathVariable UUID uuid,
            @PathVariable long timestamp
    ) {
        Instant ts = Instant.ofEpochMilli(timestamp);
        LocalDate date = LocalDate.fromMillisSinceEpoch(timestamp);
        return householdBalanceRepository.findByHouseholdIdAndDateAndTimestampAfter(uuid, date, ts);
    }
}
