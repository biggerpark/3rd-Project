package com.green.jobdone.room.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ChatPostReq {
    @Schema(title = "채팅룸 pk",example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long roomId;
    @Schema(title = "채팅 내용")
    private String contents;
    @Schema(title = "채팅 플래그 업체면1 유저면0(안보내면 자동으로 0이 들어가짐)")
    private Integer flag;

    @JsonIgnore
    private long chatId;
}
