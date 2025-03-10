package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Portfolio extends UpdatedAt{
    @Id //모든 Entity(테이블)는 PK를 가지기 때문에 무조건 포함해야하는 애노테이션이다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private long portfolioId;

    @ManyToOne
    @JoinColumn(name = "businessId")
    @OnDelete(action = OnDeleteAction.CASCADE) //단방향 상태에서 on delete cascade(DDL) 설정
    private Business business;

    @Column(nullable = false)
    private int price;

    @Column(length = 15, nullable = false)
    private String takingTime;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String contents;
}
