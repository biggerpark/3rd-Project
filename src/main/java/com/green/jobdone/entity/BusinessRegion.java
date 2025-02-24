package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "business_region")
public class BusinessRegion {
    @EmbeddedId
    private BusinessRegionIds businessRegionIds;

    @ManyToOne
    @JoinColumn(name = "businessId")
    @MapsId("businessId")
    private Business business;

    @ManyToOne
    @JoinColumn(name = "regionId")
    @MapsId("regionId")
    private Region region;
}
