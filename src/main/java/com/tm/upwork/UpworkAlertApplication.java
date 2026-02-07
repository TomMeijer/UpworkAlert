package com.tm.upwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UpworkAlertApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpworkAlertApplication.class, args);
    }
}
