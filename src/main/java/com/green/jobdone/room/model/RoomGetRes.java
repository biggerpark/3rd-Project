package com.green.jobdone.room.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomGetRes {
    private long roomId;
    private Long chatId;
    private String businessName;
    private String recentlyChat;
    private String roomCreatedAt;
    private String title;
    private String logo;
    private String pic;
    private String userName;
    @JsonIgnore
    private long userId;
    @JsonIgnore
    private long businessId;

}
