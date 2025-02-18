package com.green.jobdone.config.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.common.MyFileUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private final List<WebSocketSession> sessions = new ArrayList<>();

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket connection established: " + session.getId());
    }

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
            try{
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

                // 웹소켓 세션에 메시지 전송
                for (WebSocketSession webSocketSession : sessions) {
                    if (webSocketSession.isOpen()) {
                        try {
                            webSocketSession.sendMessage(new TextMessage("새 메세지: " + message.getPayload()));
                        } catch (IOException e) {
                            logger.error("웹소켓 메시지 전송 중 오류 발생", e);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("JSON 파싱 오류", e);
            }
            log.info("try문을 정상적으로 빠져 나왔나 확인");
            log.info("file과 req확인: {} {}", files.size(), req);
            chatService.insChat(files,req);}
            catch (Exception e) {
                log.info("다른 이유로 안되는듯");
            }
        }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket connection closed: " + session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info("handleBinaryMessage called!");
        log.info("Received binary message, size: " + message.getPayload().array().length);
        try {
            byte[] payload = message.getPayload().array();
            String jsonString = new String(payload, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonString);


            if (!jsonObject.has("roomId") || !jsonObject.has("flag")) {
                throw new RuntimeException("JSON 데이터에 roomId 또는 flag가 없습니다.");
            }

            long roomId = jsonObject.getLong("roomId");
            int flag = jsonObject.getInt("flag");


            String textMessage = jsonObject.optString("message", "").trim();

            JSONArray filesArray = jsonObject.optJSONArray("files");
            List<MultipartFile> pics = new ArrayList<>();

            // 파일이 존재할 경우에만 처리
            if (filesArray != null && filesArray.length() > 0) {
                for (int i = 0; i < filesArray.length(); i++) {
                    String base64File = filesArray.getString(i);

                    // Base64 디코딩 전에 파일이 비어있지 않은지 체크
                    if (base64File != null && !base64File.trim().isEmpty()) {
                        byte[] fileData = Base64.getDecoder().decode(base64File);
                        pics.add(convertByteArrayToMultipartFile(fileData, "uploaded_file_" + i));
                    } else {
                        log.warn("Empty or invalid Base64 file data at index: " + i);
                    }
                }
            }

            if (textMessage.isEmpty() && pics.isEmpty()) {
                throw new RuntimeException("메시지와 파일이 모두 비어 있습니다.");
            }

            ChatPostReq chatPostReq = new ChatPostReq();
            chatPostReq.setRoomId(roomId);
            chatPostReq.setContents(textMessage.isEmpty() ? null : textMessage);
            chatPostReq.setFlag(flag);
//            logger.info("ChatPostReq: " + chatPostReq);

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

