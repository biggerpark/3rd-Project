//package com.green.jobdone.config.firebase;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.green.jobdone.common.exception.CustomException;
//import com.green.jobdone.common.exception.ServiceErrorCode;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class FirebaseConfig {
//    @PostConstruct
//    public void init(){
//        try {
//            // src/main/resources/의 파일을 읽을 때는 ClassLoader를 사용하여 읽어야 함
//            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase/job-done-firebase-service.json");
//
//            if (serviceAccount == null) {
//                throw new CustomException(ServiceErrorCode.FAIL_FIREBASE);  // 파일이 없으면 예외 처리
//            }
//
//            // FirebaseOptions 초기화
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            // FirebaseApp 초기화
//            FirebaseApp.initializeApp(options);
//
//        } catch (IOException e) {
//            throw new CustomException(ServiceErrorCode.FAIL_FIREBASE);
//        }
//
//    }
//}
