package com.green.jobdone.room.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.ChatErrorCode;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Chat;
import com.green.jobdone.entity.ChatPic;
import com.green.jobdone.entity.Room;
import com.green.jobdone.room.RoomRepository;
import com.green.jobdone.room.RoomService;
import com.green.jobdone.room.chat.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public String insChat(String token ,MultipartFile pic, ChatPostReq p){
//        long userId = authenticationFacade.getSignedUserId();
//        UserIdRoom userIdRoom = chatMapper.checkUserId(p.getRoomId());
//        if(userId!=userIdRoom.getUserId()||userId!=userIdRoom.getBuid()){
//            throw new CustomException(ChatErrorCode.FAIL_TO_REG);
//        } // 채팅 인증 처리가 필요할때 사용용도
        Room room = roomRepository.findById(p.getRoomId()).orElseThrow(() -> new CustomException(ChatErrorCode.MISSING_ROOM));
        Long userId = room.getUser().getUserId();
//        if(userId!=authenticationFacade.getSignedUserId()|| !room.getBusiness().getBusinessId().equals(businessRepository.findBusinessIdByUserId(userId))){
//            throw new CustomException(ChatErrorCode.FAIL_TO_REG);
//        } //인증처리 나중에 복구해볼 생각 ㄱ
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
        if(pic==null){
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

    }

    public Long insertChat(ChatPostReq p){
        long userId = authenticationFacade.getSignedUserId();
        UserIdRoom userIdRoom = chatMapper.checkUserId(p.getRoomId());
        if(userIdRoom.getUserId()==userId || userIdRoom.getBuid()==userId){
            int res = chatMapper.insChat(p);
            return p.getChatId();
        } else {
            throw new CustomException(ChatErrorCode.FAIL_TO_REG);
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
        String logo = res.get(0).getLogo();
        long roomId = p.getRoomId();
        for (ChatGetRes chat : res) {
            long chatId = chat.getChatId();
            long businessId = chat.getBusinessId();
            List<GetPicDto> a = chat.getPics();
            chat.setLogo(PicUrlMaker.makePicUrlLogo(businessId , logo));

            if (a == null) {continue;}

            for (GetPicDto picDto : a) {
                String picName = picDto.getPic();
                picDto.setPic(PicUrlMaker.makePicUrlChat(roomId, chatId, picName));
            }
        }

        return res;
    }
}
