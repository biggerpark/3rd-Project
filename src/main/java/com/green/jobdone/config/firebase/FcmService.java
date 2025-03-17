package com.green.jobdone.config.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.green.jobdone.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FcmService {
    private final UserRepository userRepository;

    public FcmService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void sendChatNotification(Long receiverId, String messageContent) {
        String userFcmToken = getUserFcmToken(receiverId);
        // db에서 파이어베이스 토큰 조회

        if (userFcmToken == null) {
            // 토큰없으면 메세지 못보냄
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
        return userRepository.findFCMTokenById(userId);
    }
}