package com.green.jobdone.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class Portfolio extends UpdatedAt{
    @Id //모든 Entity(테이블)는 PK를 가지기 때문에 무조건 포함해야하는 애노테이션이다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private long portfolioId;

    @ManyToOne
    @JoinColumn(name = "businessId")
    @OnDelete(action = OnDeleteAction.CASCADE) //단방향 상태에서 on delete cascade(DDL) 설정
    private Business business;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioPic> portfolioPics = new ArrayList<>();

    @Column(nullable = false)
    private int price;

    @Column(length = 15, nullable = false)
    private String takingTime;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String contents;

    @Column(length = 500, nullable = false)
    private String thumbnail;

    //유튜브 영상 추가

    @Column(nullable = true,length = 255)
    private String youtubeUrl;

    // 유튜브 영상 ID 추가
    @Column(nullable = true, length = 20)
    private String youtubeId; // 영상의 ID 저장 (URL에서 추출)


}
