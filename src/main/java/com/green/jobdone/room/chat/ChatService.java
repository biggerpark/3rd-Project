package com.green.jobdone.room.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.ChatErrorCode;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.config.firebase.FcmService;
import com.green.jobdone.config.jwt.TokenProvider;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Chat;
import com.green.jobdone.entity.ChatPic;
import com.green.jobdone.entity.Room;
import com.green.jobdone.room.RoomRepository;
import com.green.jobdone.room.chat.model.*;
import com.green.jobdone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMapper chatMapper;
    private final MyFileUtils myFileUtils;
    private final AuthenticationFacade authenticationFacade;
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final ChatPicRepository chatPicRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private Map<Long, Set<WebSocketSession>> roomSessions = new HashMap<>();
    private final FcmService fcmService;

    @Transactional
    public String insChat(MultipartFile pic, ChatPostReq p){
//        long userId = authenticationFacade.getSignedUserId();
//        UserIdRoom userIdRoom = chatMapper.checkUserId(p.getRoomId());
//        if(userId!=userIdRoom.getUserId()||userId!=userIdRoom.getBuid()){
//            throw new CustomException(ChatErrorCode.FAIL_TO_REG);
//        } // 채팅 인증 처리가 필요할때 사용용도
//        Long signedUserId = null;
//        if(token!=null){
//            JwtUser jwtUser = tokenProvider.getJwtUserFromToken(token);
//            signedUserId = jwtUser.getSignedUserId();
//        } else {
//        }  이거 생각해보니 웹 소켓 접속할때 차단이 맞음 why? 아니면 실시간 채팅 염탐가능

//        log.info("토큰값: {}",token);
        Room room = roomRepository.findById(p.getRoomId()).orElseThrow(() -> new CustomException(ChatErrorCode.MISSING_ROOM));
        Long userId = room.getUser().getUserId();
        Long businessId = room.getBusiness().getBusinessId();
//        if(!userId.equals(signedUserId) || !room.getBusiness().getBusinessId().equals(signedUserId)){
//            throw new CustomException(ChatErrorCode.FAIL_TO_REG);
//        } //인증처리 나중에 복구해볼 생각 ㄱ 이거 생각해보니 웹 소켓 접속할때 차단이 맞음 why? 아니면 실시간 채팅 염탐가능
//        String logo = businessRepository.findBusinessLogoByBusinessId(businessId);
        String logo = PicUrlMaker.makePicUrlLogo(businessId,businessRepository.findBusinessLogoByBusinessId(businessId));
        String userPic = PicUrlMaker.makePicUserUrl(userId,userRepository.getUserPicByUserId(userId));
        Chat chat = new Chat();
        chat.setRoom(room);
        chat.setFlag(p.getFlag());
        chat.setContents(p.getContents());
        chatRepository.save(chat);
//        int res = chatMapper.insChat(p);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String ,Object> resJson = new HashMap<>();
        resJson.put("flag",p.getFlag());
        resJson.put("message",p.getContents());
        resJson.put("logo2",userPic);
        resJson.put("logo",logo);

        Set<WebSocketSession> sessionSet = roomSessions.get(p.getRoomId());
        boolean isReceiverInRoom = false;
        List<Integer> flags = new ArrayList<>();
        if (sessionSet != null) {
            for (WebSocketSession session : sessionSet) {
                String token = (String) session.getAttributes().get("token");
                Integer sessionFlag = (Integer) session.getAttributes().get("flag");
                flags.add(sessionFlag);
                // flag에 따라서 1이 있으면 유저 0이있으면 업체 size가 2가 아니라면(1이라면) 자기가 가진 플래그에 반대되는 사람이 없는것
            }
            if (flags.size()==2)
            {
                isReceiverInRoom = true;
            }
        }
        Long receiverId = null;
        if(p.getFlag()==1){
            receiverId = room.getBusiness().getUser().getUserId();
            log.info("업체의 유저Id 잘 들고옴? {} ", receiverId);
        } else {
            receiverId = room.getUser().getUserId();
            log.info("유저Id 잘 들고옴? {} ", receiverId);
        }
        if(!room.getState().equals("00201")){
            isReceiverInRoom = false;
        }

        if (!isReceiverInRoom) {
            fcmService.sendChatNotification(receiverId, p.getContents());
        }


        if(pic==null){
//            return null;
            try {
                return objectMapper.writeValueAsString(resJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        Long chatId = chat.getChatId();
        String filePath = String.format("room/%d/chat/%d",p.getRoomId(),chatId);
        myFileUtils.makeFolders(filePath);

        String fileName = myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        String folderPath = String.format("%s/%s", filePath,fileName);
        try {
            myFileUtils.transferTo(pic, folderPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        ChatPic chatPic = new ChatPic();
        chatPic.setChat(chat);
        chatPic.setPic(fileName);
        chatPicRepository.save(chatPic);

//        ChatPicDto chatPicDto = new ChatPicDto();
//        chatPicDto.setChatId(chatId);
//        chatPicDto.setPic(fileName);
//        int res2 = chatMapper.insChatPic(chatPicDto);
        String picUrl = String.format("/pic/%s",folderPath);
        resJson.put("pic",picUrl);
        try {
            return objectMapper.writeValueAsString(resJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        return picUrl;
    }

    public Long insertChat(ChatPostReq p){
        long userId = authenticationFacade.getSignedUserId();
        UserIdRoom userIdRoom = chatMapper.checkUserId(p.getRoomId());
        if(userIdRoom.getUserId()==userId || userIdRoom.getBuid()==userId){
            int res = chatMapper.insChat(p);
            return p.getChatId();
        } else {
            throw new CustomException(ChatErrorCode.FAIL_TO_CONNECT);
        }
    }
    public int insChatPic(List<MultipartFile> pics) {

        // chatId 가 있어야 pic 주입 가능 > chatId 는 roomId가 필요(거기서 최신 chat)
        // > 이부분도 외부에서 조회라서 안전하지 않음(flag 채크하면 되긴할듯)

        // 가져올 수 있는 정보는 jwt토큰뿐 > room 조회(여러개가 만들어져 버림_
        // 6번 userId라고 가정 그사람이 가장 최근에 친 채팅+사진> if로 UserIdRoom 의
        // userId나 buid중 같은게 있는 것 중 최신 room선택 > 그 room에서

        return 0;
    }

    public List<ChatGetRes> selRoomChat(ChatGetReq p){
        List<ChatGetRes> res = chatMapper.selRoomChat(p);
        if(res.isEmpty()){
            return res;
        }
        String logo = res.get(0).getLogo();
        long roomId = p.getRoomId();
        for (ChatGetRes chat : res) {
            long chatId = chat.getChatId();
            long businessId = chat.getBusinessId();
//            List<GetPicDto> a = chat.getPics();
            chat.setLogo(PicUrlMaker.makePicUrlLogo(businessId , logo));
            chat.setLogo2(PicUrlMaker.makePicUserUrl(chat.getUserId(),chat.getLogo2()));
            if(chat.getPic()!=null){
                chat.setPic(PicUrlMaker.makePicUrlChat(roomId,chatId,chat.getPic()));
            }

//            if (a == null) {continue;}
//
//            for (GetPicDto picDto : a) {
//                String picName = picDto.getPic();
//                picDto.setPic(PicUrlMaker.makePicUrlChat(roomId, chatId, picName));
//            }
        }
//        log.info("res: {} ", res); 로그 너무 보여

        return res;
    }
}
