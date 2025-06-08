package com.alex.web.microservices.songs.lib.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplicationRunner.class,args);
    }
}
