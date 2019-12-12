package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Battery;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.BatteryKey;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BatterySeeder {

    private BatteryRepository batteryRepository;

    @Autowired
    public BatterySeeder(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    void seed() {
        if(batteryRepository.count().block() == 0) {
            System.out.println("Seeding Batteries...");
            UUID household1 = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
            UUID household2 = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
            seedBatteryForHousehold(household1, true);
            seedBatteryForHousehold(household2, false);
        }
    }

    private void seedBatteryForHousehold(UUID household, boolean isFull) {
        Battery battery = new Battery(
                new BatteryKey(household),
                "Micro Cell",
                isFull ? 100 : 0,
                100,
                true
        );
        batteryRepository.save(battery).block();

        battery = new Battery(
                new BatteryKey(household),
                "Medium Cell",
                isFull ? 500 : 0,
                500,
                true
        );
        batteryRepository.save(battery).block();

        battery = new Battery(
                new BatteryKey(household),
                "Tesla Powerwall 1",
                isFull ? 4000 : 0,
                4000,
                true
        );
        batteryRepository.save(battery).block();

        battery = new Battery(
                new BatteryKey(household),
                "Tesla Powerwall 2",
                isFull ? 8000 : 0,
                8000,
                true
        );
        batteryRepository.save(battery).block();
    }
}
