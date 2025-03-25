package com.green.jobdone.room.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RoomDto {
    private Long userId;
    private Long businessId;
    private String state;
}
