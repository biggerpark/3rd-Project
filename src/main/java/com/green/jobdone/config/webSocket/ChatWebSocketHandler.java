package com.green.jobdone.config.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.exception.ChatErrorCode;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.room.chat.ChatMapper;
import com.green.jobdone.room.chat.ChatService;
import com.green.jobdone.room.chat.model.ChatPicDto;
import com.green.jobdone.room.chat.model.ChatPostReq;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Map<Long, Set<WebSocketSession>> roomSessions = new HashMap<>();
    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // /chat/{roomId} 식으로 나옴 지금은
        // WebSocket URI에서 roomId 추출
        String uri = session.getUri().toString();
        String[] uriParts = uri.split("/");

        // 마지막 부분이 roomId이므로, 이를 추출
        long roomId = Long.parseLong(uriParts[uriParts.length - 1]);
        session.setBinaryMessageSizeLimit(10*1024*1024);
        roomSessions.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);

        // 추출된 roomId를 사용하여 채팅방 설정 등 처리
        log.info("Room ID: " + roomId);
    }

//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.add(session);
//        log.info("WebSocket connection established: " + session.getId());
//    } 공통방

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
//        logger.info("메세지전용: " + message.getPayload());
//        long roomId = Long.parseLong(message.getPayload());
//        for (WebSocketSession webSocketSession : sessions) {
//            if (webSocketSession.isOpen()) {
//                try {
//                    webSocketSession.sendMessage(new TextMessage("새 메세지: " + message.getPayload()));
//                } catch (IOException e) {
//                    logger.error("웹소켓 메시지 전송 중 오류 발생", e);
//                }
//            }
//        }
//    }
@Override
protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("메세지전용: " + message.getPayload());
        // JSON 형식으로 파싱
        String jsonString = message.getPayload();
        log.info("jsonString: " + jsonString);
        ChatPostReq req = new ChatPostReq();
        List<MultipartFile> files = new ArrayList<>();
        try {
            log.info("try문 잘 들어오나 확인용");
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            long roomId = jsonNode.get("roomId").asLong();
            log.info("roomId: " + roomId);
            int flag = jsonNode.get("flag").asInt();
            log.info("flag: " + flag);
            String contents = jsonNode.has("message") ? jsonNode.get("message").asText().trim() : "";
            // 필요한 데이터 처리
            log.info("message: " + contents);

            req.setRoomId(roomId);
            req.setFlag(flag);
            req.setContents(contents);

            String uri = session.getUri().toString();
            String[] uriParts = uri.split("/");
            long roomId2 = Long.parseLong(uriParts[uriParts.length - 1]);

            if(roomId != roomId2) {
                throw new CustomException(ChatErrorCode.FAIL_TO_REG);
            }

            // 해당 roomId의 모든 세션에 메시지 전송
            Set<WebSocketSession> sessionSet = roomSessions.get(roomId);
            log.info("sessionSet: " + sessionSet);
            if (sessionSet != null) {
                for (WebSocketSession webSocketSession : sessionSet) {
                    if (webSocketSession.isOpen()) {
                        webSocketSession.sendMessage(new TextMessage("새 메시지: " + message.getPayload()));
                    }
                }
                        log.info("req확인: {}", req);
                        chatService.insChat(files,req);
            }
        } catch (Exception e) {
            throw new CustomException(ChatErrorCode.FAIL_TO_REG);
        }
}


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String uri = session.getUri().toString();
        String[] uriParts = uri.split("/");

        // 마지막 부분에서 roomId 추출
        long roomId = Long.parseLong(uriParts[uriParts.length - 1]);

        // 해당 roomId에 대한 세션 목록에서 제거
        Set<WebSocketSession> sessionSet = roomSessions.get(roomId);
        if (sessionSet != null) {
            sessionSet.remove(session);
            if (sessionSet.isEmpty()) {
                roomSessions.remove(roomId); // 세션이 모두 끊어지면 방을 삭제할 수 있음
            }
        }
        log.info("WebSocket connection closed in roomId: " + roomId);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info("사진 보내는곳으로 정상접속");

            byte[] payload = message.getPayload().array();
            String jsonString = new String(payload, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            if (!jsonNode.has("roomId") || !jsonNode.has("flag")) {
                throw new RuntimeException("JSON 데이터에 roomId 또는 flag가 없습니다.");
            }
            long roomId = jsonNode.get("roomId").asLong();
            int flag = jsonNode.get("flag").asInt();
            String textMessage = jsonNode.has("message") ? jsonNode.get("message").asText().trim() : "";
            log.info("textMessage 확인: {}",textMessage);

            // 파일 처리
            JsonNode filesArrayNode = jsonNode.get("files");
            List<MultipartFile> pics = new ArrayList<>();

            if (filesArrayNode != null && filesArrayNode.isArray()) {
                for (JsonNode fileNode : filesArrayNode) {
                    log.info("정상적으로 jsonNode for문 진입?");
                    String base64File = fileNode.asText().trim();
                    log.info("base64File: " + base64File);

                    // Base64 디코딩 전에 파일이 비어있지 않은지 체크
                    if (!base64File.isEmpty()) {
                        byte[] fileData = Base64.getDecoder().decode(base64File);
                        log.info("파일 디코딩하러 들어왔나 확인용");
                        pics.add(convertByteArrayToMultipartFile(fileData, "uploaded_file_" + pics.size()));
                    } else {
                        log.warn("Empty or invalid Base64 file data.");
                    }
                }
            }
            log.info("기존에 걸렸던곳 바로앞");
            log.info("textMessage: " + textMessage);
            log.info("pics: " + pics);

            if (textMessage.isEmpty() && pics.isEmpty()) {
                throw new RuntimeException("메시지와 파일이 모두 비어 있습니다.");
            }

            log.info("if문 잘 넘어갔나?");
            ChatPostReq chatPostReq = new ChatPostReq();
            chatPostReq.setRoomId(roomId);
            chatPostReq.setContents(textMessage.isEmpty() ? null : textMessage);
            chatPostReq.setFlag(flag);

            chatService.insChat(pics, chatPostReq);

            session.sendMessage(new TextMessage("파일 업로드 및 메시지 저장 완료"));

        } catch (Exception e) {
            log.error("파일 업로드 및 메시지 처리 중 오류 발생", e);
            try {
                session.sendMessage(new TextMessage("파일 업로드 및 메시지 처리 실패: " + e.getMessage()));
            } catch (IOException ex) {
                log.error("웹소켓 응답 전송 중 오류 발생", ex);
            }
        }
    }

    private MultipartFile convertByteArrayToMultipartFile(byte[] payload, String fileName) {
        return new CustomMultipartFile(payload, fileName, "application/octet-stream");
    }
}

