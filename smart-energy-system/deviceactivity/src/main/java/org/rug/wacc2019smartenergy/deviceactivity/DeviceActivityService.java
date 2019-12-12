package org.rug.wacc2019smartenergy.deviceactivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DeviceActivityService {

    public static void main(String[] args) {
        SpringApplication.run(DeviceActivityService.class, args);
    }
}