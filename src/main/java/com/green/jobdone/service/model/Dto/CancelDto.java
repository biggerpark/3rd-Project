package com.green.jobdone.service.model.Dto;

import com.green.jobdone.entity.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CancelDto {
    private String tid;
    private Long userId;
    private int price;
    private int completed;
    private LocalDate startDate;
}
