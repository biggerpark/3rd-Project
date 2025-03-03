package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio_pic")
public class PortfolioPic extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long portfolioPicId;

    @ManyToOne
    @JoinColumn(name = "portfolioId")
    @OnDelete(action = OnDeleteAction.CASCADE) //단방향 상태에서 on delete cascade(DDL) 설정
    private Portfolio portfolio;

    @Column(length = 50)
    private String pic;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int state;

}
