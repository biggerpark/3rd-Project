package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Qa extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaId;

    @ManyToOne
    @JoinColumn(name = "qaTypeDetailId", nullable = false)
    private QaType qaType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;






}
