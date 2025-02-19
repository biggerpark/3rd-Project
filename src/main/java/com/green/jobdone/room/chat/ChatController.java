package com.green.jobdone.room.chat;

import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.room.chat.model.ChatDto;
import com.green.jobdone.room.chat.model.ChatGetReq;
import com.green.jobdone.room.chat.model.ChatGetRes;
import com.green.jobdone.room.chat.model.ChatPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "7. 문의 채팅")
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = " 소켓사용시 미사용")
    @PostMapping
    public ResultResponse<Integer> inChat2(@RequestPart(required = false) MultipartFile pic, @RequestPart ChatPostReq p){
        int res = chatService.insChat(pic, p);

        return ResultResponse.<Integer>builder()
                .resultMessage("송신 완료")
                .resultData(res)
                .build();
    }
    @Operation(summary = "채팅 보내기  소켓사용시 미사용")
    @PostMapping("form-data")
    public ResultResponse<Integer> insChat(@RequestBody ChatDto p){
        MultipartFile pic = p.getPics();
        ChatPostReq req = new ChatPostReq();
        int res = chatService.insChat(pic, req);

        return ResultResponse.<Integer>builder()
                .resultMessage("송신 완료")
                .resultData(res)
                .build();
    }
    @Operation(summary = "채팅보내기  소켓사용시 미사용")
    @PostMapping("test")
    public ResultResponse<Long> insertChat(@RequestBody ChatPostReq p){
        long res = chatService.insertChat(p);
        return ResultResponse.<Long>builder()
                .resultMessage("")
                .resultData(res)
                .build();
    }
    @Operation(summary = "채팅 사진보내기 소켓사용시 미사용")
    @PostMapping("pic")
    public ResultResponse<Integer> insChat(List<MultipartFile> pics, long roomId){
        return ResultResponse.<Integer>builder()
                .resultMessage("")
                .resultData(2)
                .build();
    }


    @Operation(summary = "채팅 조회")
    @GetMapping
    public ResultResponse<List<ChatGetRes>> getRoomChat(@ParameterObject @ModelAttribute ChatGetReq p){
        List<ChatGetRes> res = chatService.selRoomChat(p);

        return ResultResponse.<List<ChatGetRes>>builder()
                .resultData(res)
                .resultMessage("조회 완료")
                .build();
    }

}
