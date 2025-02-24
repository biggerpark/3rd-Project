package com.green.jobdone.room.chat.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


import java.beans.ConstructorProperties;

@Getter


public class ChatGetReq {
    @Schema(name = "room_id")
    private Long roomId;

    @ConstructorProperties({"room_id"})
    ChatGetReq(Long roomId) {
        this.roomId = roomId;
    }
}
