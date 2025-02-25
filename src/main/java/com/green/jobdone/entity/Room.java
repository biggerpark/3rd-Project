package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Room extends CreatedAt{
    @Id //모든 Entity(테이블)는 PK를 가지기 때문에 무조건 포함해야하는 애노테이션이다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private long roomId;

    @ManyToOne
    @JoinColumn(name = "businessId")
    private Business business;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "serviceId")
    private Service service;

    @Column(nullable = false, length = 5)
    private String state="00201";
}
