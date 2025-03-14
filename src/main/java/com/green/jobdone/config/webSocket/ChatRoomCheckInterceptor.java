package com.green.jobdone.config.webSocket;



import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public class ChatRoomCheckInterceptor implements HandshakeInterceptor {
    private final StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    @Override
    public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler wsh, Map<String, Object> attributes) throws Exception {
        String uri = req.getURI().toString();
        if(!uri.matches(".*/chat/\\d+$")){
            // .* 앞에 어떤것이(도메인) 붙던지 \\d+$ 숫자로(d) 여러자리수도 가능(+) 대신 뒤에 다른게 없음($)
            res.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }
        List<String> cookies = req.getHeaders().get(HttpHeaders.COOKIE);

        if (cookies != null && !cookies.isEmpty()) {
            String cookieHeader = cookies.get(0);
            String token = null;

            String[] cookie = cookieHeader.split(";");
            for (String cookieOfToken : cookie) {
                if (cookieOfToken.trim().startsWith("accessToken=")) {
                    token = cookieOfToken.trim().substring("accessToken=".length());
                    break;
                }
            }
            if (token != null) {
                attributes.put("JWTToken", token);  // 토큰을 WebSocket 세션에 저장
            } else {
                res.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        } else {
            res.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }



        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
