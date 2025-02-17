package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class QaReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 속성이 들어감.
    private Long reasonId;

    @Column(nullable = false,length = 30, unique = true)
    private String title;



}
