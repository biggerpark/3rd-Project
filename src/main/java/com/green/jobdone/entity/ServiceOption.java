package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "service_option")
public class ServiceOption extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceOptionId;
    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;
    @ManyToOne
    @JoinColumn(name = "optionDetailId", nullable = false)
    private OptionDetail optionDetail;
    @Column(length = 300) private String comment;
    @Column private Integer price;
}
