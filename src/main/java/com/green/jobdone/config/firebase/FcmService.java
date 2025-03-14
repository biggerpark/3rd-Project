package com.green.jobdone.config.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {
    public void sendChatNotification(Long receiverId, String messageContent) {
        String userFcmToken = getUserFcmToken(receiverId);
        // db에서 파이어베이스 토큰 조회

        if (userFcmToken == null) {
            System.out.println("⚠️ FCM 토큰이 존재하지 않음. 푸시 알림 생략.");
            return;
        }

        Notification notification = Notification.builder()
                .setTitle("새로운 채팅 메시지")
                .setBody(messageContent)
                .build();

        Message message = Message.builder()
                .setToken(userFcmToken)
                .setNotification(notification)
                .build();
        // 푸시 알림 생성

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("전송 성공: " + response);
        } catch (Exception e) {
            System.out.println("전송 실패: " + e.getMessage());
        }
        // Firebase 메시지 전송
    }

    private String getUserFcmToken(Long userId) {
        // DB에서 userId에 해당하는 FCM 토큰을 조회하는 로직 필요
        return null;
    }
}