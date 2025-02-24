package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Table(name = "business_pic")
public class BusinessPic extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long businessPicId;

    @ManyToOne
    @JoinColumn(name = "businessId")
    @OnDelete(action = OnDeleteAction.CASCADE) //단방향 상태에서 on delete cascade(DDL) 설정
    private Business business;

    @Column(length = 100)
    private String pic;

    @ColumnDefault("1")
    private int state;


}
