package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdEnergyUsage;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdEnergyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HouseholdEnergyUsageSeeder {

    private HouseholdEnergyUsageRepository householdEnergyUsageRepository;

    @Autowired
    public HouseholdEnergyUsageSeeder(HouseholdEnergyUsageRepository householdEnergyUsageRepository) {
        this.householdEnergyUsageRepository = householdEnergyUsageRepository;
    }

    void seed() {
        if (householdEnergyUsageRepository.count().block() == 0) {
            System.out.println("Seeding HouseholdEnergyUsage...");
            HouseholdEnergyUsage energyUsage = new HouseholdEnergyUsage(
                    UUID.fromString("7e502970-e229-11e9-b6da-432392cdd4d9"),
                    100
            );
            householdEnergyUsageRepository.save(energyUsage).block();
        }
    }
}
