package com.alex.web.microservices.songs.lib.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplicationRunner.class, args);
    }
}
