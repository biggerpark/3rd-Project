package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "option_detail")
public class OptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionDetailId;

    @ManyToOne
    @JoinColumn(name = "optionId", nullable = false)
    private Option option;

    @Column(length = 1000) private String contents;
    @Column(nullable = false) private int price;
    @Column(length = 30, nullable = false) private String name;

}
