package org.rug.wacc2019smartenergy.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FrontEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontEndApplication.class, args);
    }
}
