package org.rug.wacc2019smartenergy.powerinfrastructureservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PowerInfrastructureService {

    public static void main(String[] args) {
        SpringApplication.run(PowerInfrastructureService.class, args);
    }
}
