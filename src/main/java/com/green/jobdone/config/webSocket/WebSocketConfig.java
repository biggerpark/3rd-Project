package com.green.jobdone.config.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat/{roomId}")
                .setAllowedOrigins(
                        "*"
//                "http://192.168.0.195:5173",   // 개발용
//                "http://112.222.157.156:5224", // 외부 도메인
//                "http://192.168.0.77:5173",
//                "http://192.168.0.134:5173"
        );
        //chat 이후에 어떤곳이라도 웹소케 헨들러 등록
    }
}
