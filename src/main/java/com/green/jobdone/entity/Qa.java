package com.green.jobdone.entity;


import com.green.jobdone.config.converter.ReportReasonConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Qa extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaId;

    @ManyToOne
    @JoinColumn(name = "qaTypeDetailId")
    private QaTypeDetail qaTypeDetail;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 3000, nullable = false)
    private String contents;

    @Column(length = 10, nullable = false)
    // 00101:미답변,00102:검토중,00103:답변완료
    private String qaState;

    @Column // 일반문의는 없으므로 NULL 허용
    private Long qaTargetId;


    @Convert(converter = ReportReasonConverter.class)  // ENUM 을 DB에 저장할 때 코드(code) 로 변환
    @Column(name = "reportReasonId") // 일반문의는 따로 없으므로 NULL 허용
    private ReportReason reportReason;

    @PrePersist
    public void prePersist() {
        if (this.qaState == null) {
            this.qaState = "00101";
        }
    }

}
