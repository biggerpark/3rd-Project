package com.green.jobdone.entity;

import com.green.jobdone.config.converter.ReportReasonConverter;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QaAnswer extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaAnswerId;

    @ManyToOne
    @JoinColumn(name = "qaId", nullable = false)
    private Qa qa;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;

    @Column(length = 3000, nullable = false)
    private String answer;



}
