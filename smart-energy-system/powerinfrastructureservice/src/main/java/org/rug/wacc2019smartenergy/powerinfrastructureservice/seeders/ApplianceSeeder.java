package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import com.netflix.discovery.converters.Auto;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Appliance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.ApplianceKey;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.ApplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApplianceSeeder {

    private ApplianceRepository applianceRepository;

    @Autowired
    public ApplianceSeeder(ApplianceRepository applianceRepository) {
        this.applianceRepository = applianceRepository;
    }

    void seed() {
        if(applianceRepository.count().block() == 0) {
            System.out.println("Seeding Appliances...");
            UUID household1 = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
            UUID household2 = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
            seedApplianceForHousehold(household1);
            seedApplianceForHousehold(household2);
        }
    }

    private void seedApplianceForHousehold(UUID household) {
        Appliance appliance = new Appliance(
                new ApplianceKey(household),
                "Fridge",
                30,
                30,
                true
        );
        applianceRepository.save(appliance).block();

        appliance = new Appliance(
                new ApplianceKey(household),
                "Lawn Mower",
                150,
                150,
                true
        );
        applianceRepository.save(appliance).block();

        appliance = new Appliance(
                new ApplianceKey(household),
                "Washing Machine",
                80,
                80,
                true
        );
        applianceRepository.save(appliance).block();

        appliance = new Appliance(
                new ApplianceKey(household),
                "Dryer",
                50,
                50,
                true
        );
        applianceRepository.save(appliance).block();

        appliance = new Appliance(
                new ApplianceKey(household),
                "Electric Boiler",
                10000,
                10000,
                true
        );
        applianceRepository.save(appliance).block();
    }
}
