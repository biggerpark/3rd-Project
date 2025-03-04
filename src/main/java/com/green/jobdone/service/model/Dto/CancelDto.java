package com.green.jobdone.service.model.Dto;

import com.green.jobdone.entity.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CancelDto {
    private String tid;
    private Long userId;
    private int price;
    private int completed;
}
