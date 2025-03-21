package com.green.jobdone.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@DynamicUpdate
public class Business extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long businessId;

    @ManyToOne
    @JoinColumn(name = "detailTypeId", nullable = false)
    private DetailType detailType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 10, name = "businessNum")
    @Size(min = 10, max = 10, message = "사업자 번호는 10자리여야 합니다.")
    private String businessNum;

    @Column(length = 20, name = "businessName")
    private String businessName;

    @Column(length = 80)//ㅏㅏ
    private String title;

    @Column(length = 3000)
    private String contents;

    @Column(length = 100)
    private String logo;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(nullable = false, name = "busiCreatedAt")
    private LocalDate busiCreatedAt;

    @Column(name = "approveAt")
    private LocalDate approveAt;

    @Column(name = "openingTime")
    private LocalTime openingTime;

    @Column(name = "closingTime")
    private LocalTime closingTime;

    @Column(length = 100)
    private String paper;

    @Column(length = 13, nullable = false)
    private String tel;

    @Column(nullable = false)
    @Builder.Default
    private int state = 100;

    @Column(length = 13)
    private String safeTel;

    @Column
    @JsonIgnore
    private Double lat;

    @Column
    @JsonIgnore
    private Double lng;


    @Column(length = 200)
    private String rejectContents;

    @Column(length = 200)
    private String thumbnail;



    public Business() {

    }
}
