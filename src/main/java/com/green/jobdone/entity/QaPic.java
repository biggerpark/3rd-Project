package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class QaPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaPicId;

    @ManyToOne
    @JoinColumn(name = "qaId", nullable = false)
    private Qa qa;

    @Column(length = 50, nullable = false)
    private String pic;
}
