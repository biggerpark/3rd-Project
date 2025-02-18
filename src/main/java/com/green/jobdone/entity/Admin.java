package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Admin  extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 속성이 들어감.
    private Long adminId;

    @Column(nullable = false,length = 30,unique = true) // not null, unique 속성 추가
    private String aId;

    @Column(nullable = false,length = 100)
    private String aPw;

    @Column(length = 30, nullable = false)
    private String name;


    @Column(length = 11,nullable = false)
    private String phone;

//    @Column(nullable = false)
//    private int type=100;

}
