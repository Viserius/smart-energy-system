package org.rug.wacc2019smartenergy.deviceactivity;

import org.rug.wacc2019smartenergy.deviceactivity.model.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@EnableScheduling
@RestController
public class HouseholdEnergyActivityProducer {

    private RestTemplate restTemplate;

    private AmqpTemplate amqpTemplate;

    private TopicExchange exchangeSingle;

    private FanoutExchange exchangeFanOut;

    private Queue queueEnergyActivity;

    @Value("${services.gateway.householdurl}")
    private String uri;

    private static Random random = new Random();

    @Autowired
    public HouseholdEnergyActivityProducer(
            RestTemplate restTemplate,
            AmqpTemplate amqpTemplate,
            TopicExchange exchangeSingle,
            FanoutExchange exchangeFanOut,
            Queue queueEnergyActivity) {
        this.restTemplate = restTemplate;
        this.amqpTemplate = amqpTemplate;
        this.exchangeSingle = exchangeSingle;
        this.exchangeFanOut = exchangeFanOut;
        this.queueEnergyActivity = queueEnergyActivity;
    }

    @Scheduled(fixedDelay = 30000)
    public void updateEnergyUsage() {
        try {
            Household[] households = restTemplate.getForObject(uri, Household[].class);
            if (households != null) {
                List<Household> householdList = Arrays.asList(households);
                householdList.forEach(household -> {
                    Appliance[] appliances = restTemplate.getForObject(
                            uri + "/" + household.getId().toString() + "/appliances",
                            Appliance[].class
                    );
                    Generator[] generators = restTemplate.getForObject(
                            uri + "/" + household.getId().toString() + "/generators",
                            Generator[].class
                    );
                    float newEnergyUsage = 0;
                    float newEnergyProduction = 0;
                    if (appliances != null || generators != null) {
                        List<Appliance> applianceList = Arrays.asList(appliances);
                        double averageEnergyUsage = applianceList.stream().mapToDouble(
                                        appliance -> appliance.isEnabled() ? appliance.getAveragePowerUsage() : 0
                                ).sum() * 30;
                        newEnergyUsage =
                                (float)(Math.max(
                                        averageEnergyUsage
                                        * (1 + (random.nextBoolean() ? -1 : 1) * (0.2 * random.nextFloat())),
                                        0
                                ));
                        List<Generator> generatorList = Arrays.asList(generators);
                        double averageEnergyProduction = generatorList.stream().mapToDouble(
                                generator -> generator.isEnabled() ? generator.getAveragePowerProduction() : 0
                        ).sum() * 30;
                        newEnergyProduction =
                                (float)(Math.max(
                                        averageEnergyProduction
                                                * (1 + (random.nextBoolean() ? -1 : 1) * (0.2 * random.nextFloat())),
                                        0
                                ));
                    }
                    HouseholdEnergyActivity history = new HouseholdEnergyActivity(
                            household.getId(),
                            newEnergyUsage,
                            newEnergyProduction
                    );
                    amqpTemplate.convertAndSend(exchangeSingle.getName(), queueEnergyActivity.getName(), history);
                    amqpTemplate.convertAndSend(exchangeFanOut.getName(), ".", history);
                });
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}
