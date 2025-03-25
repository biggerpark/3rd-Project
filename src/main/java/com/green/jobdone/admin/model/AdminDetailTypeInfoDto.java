package com.green.jobdone.admin.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailTypeInfoDto {
    private Long detailTypeId;
    private String detailTypeName;
    private int count;
    private double detailTypePercent;
}
