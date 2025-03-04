package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_auth")
public class UserAuth  {
    @EmbeddedId
    private UserAuthIds userAuthIds;
    private LocalDateTime expiredTime;
    @Column(length = 10) private String authCode;
    private int authCheck;
}
