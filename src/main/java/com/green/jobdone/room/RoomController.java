package com.green.jobdone.room;

import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.room.model.RoomGetReq;
import com.green.jobdone.room.model.RoomGetRes;
import com.green.jobdone.room.model.RoomPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("room")
@Tag(name = "6. 문의 관련")
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "채팅방 조회")
    @GetMapping
    public ResultResponse<List<RoomGetRes>> getRooms(@ParameterObject @ModelAttribute RoomGetReq p) {
        List<RoomGetRes> res = roomService.selRoom(p);
        return ResultResponse.<List<RoomGetRes>>builder()
                .resultData(res)
                .resultMessage("조회 완료")
                .build();
    }
    @Operation(summary = "채팅방 생성")
    @PostMapping
    public ResultResponse<Long> insRoom(@RequestBody RoomPostReq p) {
        Long res = roomService.insRoom(p);
        return ResultResponse.<Long>builder()
                .resultData(res)
                .resultMessage("생성 완료")
                .build();
    }
}
