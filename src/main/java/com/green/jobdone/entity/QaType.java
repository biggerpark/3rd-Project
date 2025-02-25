package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class QaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaTypeId;


    @Column(nullable = false, length = 30) // 환불요청(서비스 불만족),업체문의,유저문의,업체를 신고, 리뷰를 신고, 채팅을 신고
    private String type;
}
