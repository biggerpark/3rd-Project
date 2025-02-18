package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Category extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 속성이 들어감.
    private Long categoryId;

    @Column(nullable = false,length = 20) // not null, unique 속성 추가
    private String categoryName;



}
