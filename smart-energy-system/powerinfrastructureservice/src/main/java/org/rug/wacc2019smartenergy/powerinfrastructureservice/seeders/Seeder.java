package org.rug.wacc2019smartenergy.powerinfrastructureservice.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private ApplianceSeeder applianceSeeder;
    private GeneratorSeeder generatorSeeder;
    private BatterySeeder batterySeeder;
    private HouseholdSeeder householdSeeder;
    private HouseholdEnergyUsageSeeder householdEnergyUsageSeeder;

    @Autowired
    public Seeder(ApplianceSeeder applianceSeeder,
                  GeneratorSeeder generatorSeeder,
                  BatterySeeder batterySeeder,
                  HouseholdSeeder householdSeeder,
                  HouseholdEnergyUsageSeeder householdEnergyUsageSeeder
    ) {
        this.applianceSeeder = applianceSeeder;
        this.generatorSeeder = generatorSeeder;
        this.batterySeeder = batterySeeder;
        this.householdSeeder = householdSeeder;
        this.householdEnergyUsageSeeder = householdEnergyUsageSeeder;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        System.out.println("Initializing Seeders...");
        applianceSeeder.seed();
        generatorSeeder.seed();
        batterySeeder.seed();
        householdSeeder.seed();
//        powerUsageHistorySeeder.seed();
    }
}
