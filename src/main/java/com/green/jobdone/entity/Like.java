package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "`Like`")
public class Like {
    @EmbeddedId
    private LikeIds likeIds;

    @ManyToOne
    @JoinColumn(name = "businessId")
    @MapsId("businessId")
    private Business business;

    @ManyToOne
    @JoinColumn(name = "userId")
    @MapsId("userId")
    private User user;
}
