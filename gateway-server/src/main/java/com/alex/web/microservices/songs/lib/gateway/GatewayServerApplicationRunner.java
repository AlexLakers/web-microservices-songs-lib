package com.alex.web.microservices.songs.lib.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayServerApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplicationRunner.class, args);
    }
}
