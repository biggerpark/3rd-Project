package com.green.jobdone.entity;

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

    @Column(length = 500, nullable = false)
    private String contents;

    @Column(nullable = false)
    @ColumnDefault("101") // 101:미답변,102:검토중,103:답변완료
    private int qaState;

    @Column
    private long qaTargetId;






}
