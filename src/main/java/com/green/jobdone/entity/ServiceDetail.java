package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "service_detail")
public class ServiceDetail extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;
    @OneToOne
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;

    @Column(nullable = false) private LocalDate startDate;
    @Column private LocalDate endDate;
    @Column private LocalTime mStartTime;
    @Column private LocalTime mEndTime;
    @Column private LocalTime sTime;
    @Column private LocalTime eTime;
    @Column(nullable = false) @ColumnDefault("0") private int allow;

}
