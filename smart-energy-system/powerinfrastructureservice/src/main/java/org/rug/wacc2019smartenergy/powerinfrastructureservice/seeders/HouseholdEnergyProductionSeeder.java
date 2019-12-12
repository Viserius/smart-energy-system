package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.HouseholdEnergyProduction;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdEnergyProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HouseholdEnergyProductionSeeder {

    private HouseholdEnergyProductionRepository householdEnergyProductionRepository;

    @Autowired
    public HouseholdEnergyProductionSeeder(HouseholdEnergyProductionRepository householdEnergyProductionRepository) {
        this.householdEnergyProductionRepository = householdEnergyProductionRepository;
    }

    void seed() {
        if (householdEnergyProductionRepository.count().block() == 0) {
            System.out.println("Seeding HouseholdEnergyProduction...");
            HouseholdEnergyProduction energyUsage = new HouseholdEnergyProduction(
                    UUID.fromString("7e502970-e229-11e9-b6da-432392cdd4d9"),
                    100
            );
            householdEnergyProductionRepository.save(energyUsage).block();
        }
    }
}
