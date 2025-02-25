package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "qa_type_detail")
public class QaTypeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaTypeDetailId;

    @ManyToOne
    @JoinColumn(name = "qaTypeId")
    private QaType qaType;

    @Column(nullable = false, length = 30)
    private String reason;
}
