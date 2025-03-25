package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "option", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OptionDetail> optionDetails = new ArrayList<>();
}
