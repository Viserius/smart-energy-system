package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Generator;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.GeneratorKey;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Household;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.HouseholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class HouseholdSeeder {

    private HouseholdRepository householdRepository;

    @Autowired
    public HouseholdSeeder(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
    }

    void seed() {
        householdRepository.deleteAll().block();
        if(householdRepository.count().block() == 0) {
            System.out.println("Seeding Households...");
            Household household = new Household(
                    UUID.fromString("123e4567-e89b-12d3-a456-426655440000"),
                    "Costa Plenty",
                    false,
                    new BigDecimal(1),
                    new BigDecimal(1.5),
                    new BigDecimal(0.5),
                    new BigDecimal(0)
            );
            householdRepository.save(household).block();

            household = new Household(
                    UUID.fromString("123e4567-e89b-12d3-a456-426655440001"),
                    "Hollow Pickle",
                    false,
                    new BigDecimal(1),
                    new BigDecimal(1.5),
                    new BigDecimal(0.5),
                    new BigDecimal(0)
            );
            householdRepository.save(household).block();
        }
    }
}
