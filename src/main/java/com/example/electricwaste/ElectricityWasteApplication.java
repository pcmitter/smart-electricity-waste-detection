package com.example.electricwaste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElectricityWasteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectricityWasteApplication.class, args);
    }
}