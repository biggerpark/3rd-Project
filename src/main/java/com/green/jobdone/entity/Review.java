package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Review extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewPic> reviewPics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;


    @Column(length = 30000)
    private String contents;

    @Column(nullable = false)
    private double score;




}
