package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Setter
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(nullable = false, length = 30) private String name;
}
