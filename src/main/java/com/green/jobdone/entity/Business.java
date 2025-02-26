package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString

public class Business extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long businessId;

    @ManyToOne
    @JoinColumn(name = "detailTypeId", nullable = false)
    private DetailType detailType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 10, name = "businessNum")
    private String businessNum;

    @Column(length = 20, name = "businessName")
    private String businessName;

    @Column(length = 80)
    private String title;

    @Column(length = 3000)
    private String contents;

    @Column(length = 100)
    private String logo;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(nullable = false, name = "busiCreatedAt")
    private LocalDate busiCreatedAt;

    @Column(name = "openingTime")
    private LocalTime openingTime;

    @Column(name = "closingTime")
    private LocalTime closingTime;

    @Column(length = 100)
    private String paper;

    @Column(length = 13, nullable = false)
    private String tel;

    @Column(nullable = false)
    private int state = 100;

    @Column(length = 13)
    private String safeTel;

    @Column
    private Double lat;

    @Column
    private Double lng;


    @Column(length = 200)
    private String rejectContents;




}
