package com.green.jobdone.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class LikeIds implements Serializable {
    private Long businessId;
    private Long userId;
}
