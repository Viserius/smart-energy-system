package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Appliance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.ApplianceKey;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Generator;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.GeneratorKey;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.ApplianceRepository;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.GeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GeneratorSeeder {

    private GeneratorRepository generatorRepository;

    @Autowired
    public GeneratorSeeder(GeneratorRepository generatorRepository) {
        this.generatorRepository = generatorRepository;
    }

    void seed() {
        if(generatorRepository.count().block() == 0) {
            System.out.println("Seeding Generators...");
            UUID household1 = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
            UUID household2 = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
            seedGeneratorForHousehold(household2, true);
            seedGeneratorForHousehold(household1, false);
        }
    }

    private void seedGeneratorForHousehold(UUID household, boolean enabled) {
        Generator generator = new Generator(
                new GeneratorKey(household),
                "Wind mill",
                600,
                600,
                enabled
        );
        generatorRepository.save(generator).block();

        generator = new Generator(
                new GeneratorKey(household),
                "Solar Panel",
                360,
                360,
                enabled
        );
        generatorRepository.save(generator).block();

        generator = new Generator(
                new GeneratorKey(household),
                "Solar Panel",
                360,
                360,
                enabled
        );
        generatorRepository.save(generator).block();
//
//        generator = new Generator(
//                new GeneratorKey(household),
//                "Nuclear Reactor",
//                1250000000,
//                1250000000,
//                true
//        );
//        generatorRepository.save(generator).block();
    }
}
