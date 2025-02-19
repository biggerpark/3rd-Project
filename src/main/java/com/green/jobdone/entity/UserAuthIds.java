package com.green.jobdone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class UserAuthIds implements Serializable {
    private String email;
    @CreatedDate
    @Column(columnDefinition = "DATETIME(0)")
    private LocalDateTime createdAt;
}
