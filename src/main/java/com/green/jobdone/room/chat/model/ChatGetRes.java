package com.green.jobdone.room.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ChatGetRes {
    private int flag;
    private long chatId;
    private String userName;
    private String businessName;
    @JsonIgnore
    private long businessId;
    @JsonIgnore
    private long userId;
    private String logo;
    private String logo2; // 유저pic임 다른거 문제같아서 이름좀 바꿔본 것 하지만 이미 이름 통일해서 그냥 이거로 사용
    private String createdAt;
    private String message;
    private String pic;
    private Long chatPicId;
//    private List<GetPicDto> pics;
}
