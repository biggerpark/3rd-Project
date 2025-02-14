package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Many;

//@Entity
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
    @ManyToOne
//    @JoinColumn(name = "", nullable = false)
//    private Business business;
//
//    @ManyToOne
//    @JoinColumn(name = "", nullable = false)
//    private DetailType detailType;

    @Column
    private int price;
}
