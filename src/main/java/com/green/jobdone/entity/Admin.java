package com.green.jobdone.entity;

import com.green.jobdone.config.converter.UserRoleConverter;
import com.green.jobdone.config.jwt.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
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



    @Convert(converter = UserRoleConverter.class)
    @ColumnDefault("101")  // DB 기본값 101 (Admin)
    @Column(name="type",nullable = false)
    private UserRole role;


}
