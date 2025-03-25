package com.green.jobdone.service.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ServiceQaDto {
    private int completed;
    private LocalDateTime doneAt;
    private LocalDateTime paidAt;
}
