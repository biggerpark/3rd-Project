package com.green.jobdone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class BusinessPicIds implements Serializable {
    private Long businessId;
    @Column(length = 100)
    private String pic;
}
