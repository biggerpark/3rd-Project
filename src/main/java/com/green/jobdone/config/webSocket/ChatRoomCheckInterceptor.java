package com.green.jobdone.config.webSocket;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class ChatRoomCheckInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler wsh, Map<String, Object> attributes) throws Exception {
        String uri = req.getURI().toString();
        if(!uri.matches(".*/chat/\\d+$")){
            // .* 앞에 어떤것이(도메인) 붙던지 \\d+$ 숫자로(d) 여러자리수도 가능(+) 대신 뒤에 다른게 없음($)
            res.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
