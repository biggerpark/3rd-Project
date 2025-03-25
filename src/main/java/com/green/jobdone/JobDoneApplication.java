package com.green.jobdone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableJpaAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableScheduling
public class JobDoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobDoneApplication.class, args);
    }

}
