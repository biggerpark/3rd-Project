package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Qa extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaId;

    @ManyToOne
    @JoinColumn(name = "qaTypeDetailId")
    private QaTypeDetail qaTypeDetail;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, length = 3000)
    private String contents;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int qaState;
}
