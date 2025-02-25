package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "qa_type")

public class QaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaTypeId;

    @Column(nullable = false, length = 30) // 환불요청(서비스 불만족),업체문의,유저문의,업체를 신고, 리뷰를 신고, 채팅을 신고
    private String type;
}
