package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
public class Etc extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long etcId;

    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;

    @Column private int price;
    @Column(length = 2000) private String comment;
}
