package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "common_code")
public class CommonCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commonCodeId;

    @Column(nullable = false,length = 3)
    private String tableCode;
    @Column(length = 5)
    private String stateCode;
    @Column(nullable = false, length = 30)
    private String codeName;
    @Column(length = 2000)
    private String description;

}
