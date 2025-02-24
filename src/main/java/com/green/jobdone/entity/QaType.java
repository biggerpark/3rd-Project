package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaTypeId;

    @Column(nullable = false, length = 30)
    private String type;
}
