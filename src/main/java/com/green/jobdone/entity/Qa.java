package com.green.jobdone.entity;

import com.green.jobdone.config.converter.ReportReasonConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
public class Qa extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaId;

    @ManyToOne
    @JoinColumn(name = "qaTypeDetailId", nullable = false)
    private QaType qaType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 3000, nullable = false)
    private String contents;

    @Column(nullable = false)
    @ColumnDefault("101") // 101:미답변,102:검토중,103:답변완료
    private int qaState;

    @Column
    private Long qaTargetId;


    @Convert(converter = ReportReasonConverter.class)  // ENUM 을 DB에 저장할 때 코드(code) 로 변환
    @Column(name = "reportReasonId")
    private ReportReason reportReason;



}
