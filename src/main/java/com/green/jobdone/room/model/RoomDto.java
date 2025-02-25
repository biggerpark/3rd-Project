package com.green.jobdone.room.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomDto {
    private long userId;
    private long businessId;
    private String state;
}
