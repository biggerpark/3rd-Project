package com.green.jobdone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JobDoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobDoneApplication.class, args);
    }

}
