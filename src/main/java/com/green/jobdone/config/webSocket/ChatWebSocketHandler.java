package com.green.jobdone.config.webSocket;

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
    @Autowired
    private ChatService chatService;


    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        logger.info("WebSocket connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("Received message: " + message.getPayload());

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                try {
                    webSocketSession.sendMessage(new TextMessage("새 메세지: " + message.getPayload()));
                } catch (IOException e) {
                    logger.error("웹소켓 메시지 전송 중 오류 발생", e);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        logger.info("WebSocket connection closed: " + session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        logger.info("handleBinaryMessage called!");
        logger.info("Received binary message, size: " + message.getPayload().array().length);
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
                        logger.warn("Empty or invalid Base64 file data at index: " + i);
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
            logger.error("파일 업로드 및 메시지 처리 중 오류 발생", e);
            try {
                session.sendMessage(new TextMessage("파일 업로드 및 메시지 처리 실패: " + e.getMessage()));
            } catch (IOException ex) {
                logger.error("웹소켓 응답 전송 중 오류 발생", ex);
            }
        }
    }

    private MultipartFile convertByteArrayToMultipartFile(byte[] payload, String fileName) {
        return new CustomMultipartFile(payload, fileName, "application/octet-stream");
    }
}

