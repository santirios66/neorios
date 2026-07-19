package com.neorios.neorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NeoriosApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoriosApplication.class, args);
    }
}