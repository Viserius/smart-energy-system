package org.rug.wacc2019smartenergy.powerinfrastructureservice.controllers;

import com.datastax.driver.core.LocalDate;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.WebsocketClients;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.*;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.ArrayList;
import java.util.UUID;

@CrossOrigin
@RestController
public class HouseholdEnergyActivityController {

    private SimpMessagingTemplate template;
    private WebsocketClients websocketClients;

    private BatteriesController batteriesController;
    private HouseholdsController householdsController;

    private HouseholdEnergyUsageRepository householdEnergyUsageRepository;
    private HouseholdEnergyProductionRepository householdEnergyProductionRepository;
    private HouseholdBalanceRepository householdBalanceRepository;
    private BatteryRepository batteryRepository;

    @Autowired
    public HouseholdEnergyActivityController(
            SimpMessagingTemplate template,
            WebsocketClients websocketClients,
            BatteriesController batteriesController,
            HouseholdsController householdsController,
            HouseholdEnergyUsageRepository householdEnergyUsageRepository,
            HouseholdEnergyProductionRepository householdEnergyProductionRepository,
            HouseholdBalanceRepository householdBalanceRepository,
            BatteryRepository batteryRepository
    ) {
        this.template = template;
        this.websocketClients = websocketClients;
        this.batteriesController = batteriesController;
        this.householdsController = householdsController;
        this.householdEnergyUsageRepository = householdEnergyUsageRepository;
        this.householdEnergyProductionRepository = householdEnergyProductionRepository;
        this.householdBalanceRepository = householdBalanceRepository;
        this.batteryRepository = batteryRepository;
    }

    /**
     * This method removes an item from the queue. Every item is only processed by a single consumer.
     * @param energyActivity Object containing current energy production and usage of a household
     */
    @RabbitListener(queues = "${jsa.rabbitmq.queue_energy_activity}", containerFactory = "jsaFactory")
    public void receivedOverSingleQueue(HouseholdEnergyActivity energyActivity) {

        // Compute net power usage over the last X seconds
        double netEnergyUsageKWH = energyActivity.getNetEnergyUsage();
        System.out.println("Net energy usage in KWH pre-battery: " + netEnergyUsageKWH);

        // Store power in household's own battery or draw power from it
        netEnergyUsageKWH -= batteriesController.drawPowerFromHouseholdBattery(energyActivity.getHouseholdId(),
                netEnergyUsageKWH, false);
        System.out.println("Net energy usage in KWH post-battery: " + netEnergyUsageKWH);

        // If still positive energy usage after taking from battery, purchase the remaining energy
        // Otherwise, if still negative energy usage (surplus), sell the remaining energy
        if(netEnergyUsageKWH >= 0)
            this.householdsController.purchaseEnergy(
                    energyActivity.getHouseholdId(),
                    energyActivity.getTimestamp(),
                    netEnergyUsageKWH
            );
        else if(netEnergyUsageKWH <= 0)
            this.householdsController.sellEnergy(
                    energyActivity.getHouseholdId(),
                    energyActivity.getTimestamp(),
                    -netEnergyUsageKWH
            );

        // Save Production and Usage of the current timeframe for persistence
        householdEnergyUsageRepository.save(new HouseholdEnergyUsage(
                energyActivity.getHouseholdId(),
                energyActivity.getDate(),
                energyActivity.getTimestamp(),
                energyActivity.getEnergyUsage()
        )).subscribe();
        householdEnergyProductionRepository.save(new HouseholdEnergyProduction(
                energyActivity.getHouseholdId(),
                energyActivity.getDate(),
                energyActivity.getTimestamp(),
                energyActivity.getEnergyProduction()
        )).subscribe();

    }

    /**
     * This method only reads an item from the queue. Every item is processed (broadcast) by all consumers.
     * @param energyActivity Object containing current energy production and usage of a household
     */
    @RabbitListener(queues = "#{autoDeleteQueue.name}", containerFactory = "jsaFactory")
    public void receivedOverFanOutQueue(HouseholdEnergyActivity energyActivity) {
        String householdId = energyActivity.getHouseholdId().toString();
        // TODO Do something to generate new battery state/balance, don't save it (do that in the singlequeue)
        this.batteryRepository.findByKeyHouseholdId(UUID.fromString(householdId)).collectList().subscribe(batteries -> {
            this.websocketClients.getHouseholdSessions(householdId).forEach(sessionId -> {
                MessageHeaders headers = this.createHeaders(sessionId);
                template.convertAndSendToUser(
                        sessionId,
                        "queue/energy-storage",
                        batteries,
                        headers
                );
            });
        });
        this.websocketClients.getHouseholdSessions(householdId).forEach(sessionId -> {
            MessageHeaders headers = this.createHeaders(sessionId);
            template.convertAndSendToUser(
                    sessionId,
                    "queue/energy-usage",
                    new DataPoint(
                            energyActivity.getTimestamp().toEpochMilli(),
                            energyActivity.getEnergyUsage()
                    ),
                    headers
            );
            template.convertAndSendToUser(
                    sessionId,
                    "queue/energy-production",
                    new DataPoint(
                            energyActivity.getTimestamp().toEpochMilli(),
                            energyActivity.getEnergyProduction()
                    ),
                    headers
            );

            this.householdBalanceRepository.findFirstByHouseholdIdAndDate(
                    energyActivity.getHouseholdId(),
                    LocalDate.fromMillisSinceEpoch(energyActivity.getTimestamp().toEpochMilli())
            ).subscribe(householdBalance -> {
                template.convertAndSendToUser(
                        sessionId,
                        "queue/balance",
                        new DataPoint(
                                energyActivity.getTimestamp().toEpochMilli(),
                                householdBalance.getValue()
                        ),
                        headers
                );
            });
        });
    }

    @MessageMapping("/register")
    public void register(@Payload String householdId, SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        this.websocketClients.deregisterHousehold(sessionId);
        this.websocketClients.registerHousehold(sessionId, householdId);
    }

    @MessageMapping("/deregister")
    public void deregister(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        this.websocketClients.deregisterHousehold(sessionId);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
