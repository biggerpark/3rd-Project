package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class QaTypeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaTypeDetailId;

    @ManyToOne
    @JoinColumn(name = "qaTypeId", nullable = false)
    private QaType qaType;

    @Column(nullable = false, length = 50) // 이유는 윤석이 피그마에 잘 정리해둠.
    private String reason;

}
