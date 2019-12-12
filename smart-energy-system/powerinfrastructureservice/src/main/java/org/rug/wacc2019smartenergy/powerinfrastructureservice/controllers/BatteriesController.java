package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import com.google.common.util.concurrent.AtomicDouble;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Battery;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class BatteriesController {

    @Value("${powerprocessing.minimum_energy_unit}")
    private double MINIMUM_ENERGY_UNIT=1E-7;

    @Value("${powerprocessing.percentage_allowed_for_trading}")
    private float PERCENTAGE_ALLOWED_FOR_TRADING=0.5f;

    private BatteryRepository batteryRepository;

    @Autowired
    public BatteriesController(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    @CrossOrigin
    @RequestMapping(method=GET, value="/households/{uuid}/batteries")
    public Flux<Battery> batteries(@PathVariable UUID uuid) {
        return batteryRepository.findByKeyHouseholdId(uuid);
    }

    @CrossOrigin
    @RequestMapping(method=GET, value="/households/{uuid}/batteries/{id}")
    public Mono<Battery> battery(
            @PathVariable
            @Min(value = 0, message = "id must be non-negative.") // to check if in browser or exception
                    UUID id,
            @PathVariable UUID uuid
    ) {
        return this.batteryRepository.findByKeyHouseholdIdAndKeyId(uuid, id);
    }

    @CrossOrigin
    @PutMapping(value = "/households/{uuid}/batteries/{id}")
    public Mono<Battery> put(@PathVariable UUID uuid,
                               @PathVariable UUID id,
                               @RequestBody Battery battery) {
        return batteryRepository.save(battery);
    }

    /**
     * If positive and internal (not to external household): Deplete batteries, possibly until empty
     * If positive and power goes to external household: Deplete batteries until max. 50% empty
     * If negative, excess power is fed in the batteries.
     * @param householdId Household to draw from (or possibly supply to)
     * @param energyToSubtract Amount of energy
     * @param toExternalHousehold True=energy goes to other household, false=energy goes to its own household
     */
    public double drawPowerFromHouseholdBattery(UUID householdId, double energyToSubtract, boolean toExternalHousehold) {
        // Allow variable to be used inside lambda
        AtomicDouble energyToSubtractAtomic = new AtomicDouble(energyToSubtract);

        // Obtain all batteries of this household
        Flux<Battery> batteryFlux = batteries(householdId);

        // Process each battery 1 by 1, until there is no more energy to subtract (or store)
        batteryFlux.doOnNext(battery -> {
            // If using or generating less than 1 watt/sec, don't calculate
            if(Math.abs(energyToSubtractAtomic.get()) < this.MINIMUM_ENERGY_UNIT){ return; }

            double energyToSubtractCurrent = 0;

            // Drawing power for internal use from the battery
            if(energyToSubtractAtomic.get() > 0 && !toExternalHousehold
                    && battery.getCurrentPowerStorage() >= this.MINIMUM_ENERGY_UNIT
                    && battery.isEnabled()) {
                energyToSubtractCurrent = Math.min(battery.getCurrentPowerStorage(), energyToSubtractAtomic.get());
            }

            // Drawing power for external use from the battery
            else if(energyToSubtractAtomic.get() > 0 && toExternalHousehold
                    && battery.getCurrentPowerStorage() >= this.MINIMUM_ENERGY_UNIT
                    && battery.isEnabled()) {
                double energyAvailable =
                        (battery.getCurrentPowerStorage() >= battery.getTotalPowerStorage()*PERCENTAGE_ALLOWED_FOR_TRADING)
                                ? battery.getCurrentPowerStorage() - battery.getTotalPowerStorage()*PERCENTAGE_ALLOWED_FOR_TRADING
                                : 0;
                double energyToTransferFromBattery = Math.min(energyAvailable, energyToSubtractAtomic.get());

                // Make sure the tradeable amount of energy is larger than minimum amount of energy
                if(energyToTransferFromBattery <= MINIMUM_ENERGY_UNIT) {return;}

                // Subtract the amount
                energyToSubtractCurrent = energyToTransferFromBattery;
            }

            // Storing power into the battery
            else if(energyToSubtractAtomic.get() < 0
                    && battery.getTotalPowerStorage() - battery.getCurrentPowerStorage() >= this.MINIMUM_ENERGY_UNIT
                    && battery.isEnabled()) {
                double energyToStore = Math.min(
                        Math.abs(energyToSubtractAtomic.get()),
                        battery.getTotalPowerStorage() - battery.getCurrentPowerStorage()
                );
                energyToSubtractCurrent = -energyToStore;
            }

            // Remove the subtracted amount from the global counter
            energyToSubtractAtomic.set(energyToSubtractAtomic.get() - energyToSubtractCurrent);

            // Update state of the battery
            battery.setCurrentPowerStorage(battery.getCurrentPowerStorage() - energyToSubtractCurrent);
            batteryRepository.save(battery).subscribe();
        }).blockLast();

        // The amount of energy that was subtracted from the battery
        return energyToSubtract - energyToSubtractAtomic.get();
    }
}
