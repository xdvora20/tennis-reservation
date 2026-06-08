package com.example.tennisreservation;

import com.example.tennisreservation.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TennisReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennisReservationApplication.class, args);
    }

}
