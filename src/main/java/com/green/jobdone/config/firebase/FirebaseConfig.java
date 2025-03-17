package com.green.jobdone.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ServiceErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init(){
        try{
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/job-done-firebase-service.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
        } catch (IOException e){
            throw new CustomException(ServiceErrorCode.FAIL_FIREBASE);
        }
    }
}
