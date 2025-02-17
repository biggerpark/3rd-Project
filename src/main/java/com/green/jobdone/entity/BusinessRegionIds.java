package com.green.jobdone.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode

public class BusinessRegionIds implements Serializable {
    private Long businessId;
    private Long regionId;
}
