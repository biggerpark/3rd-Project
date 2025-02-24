package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QaAnswer extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaAnswerId;

    @ManyToOne
    @JoinColumn(name = "qaId")
    private Qa qa;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;

    @Column(nullable = false,length = 3000)
    private String answer;

}
