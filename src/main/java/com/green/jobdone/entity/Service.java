package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Service extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(nullable = false) private int price;
    @Column(length = 6,nullable = false) private double lat;
    @Column(length = 6,nullable = false) private double lng;
    @Column(length = 50,nullable = false) private String address;
    @Column(length = 1000) private String comment;
    @Column(nullable = false) @ColumnDefault("0") private int completed;
    @Column(length = 3000) private String addComment;
    @Column(nullable = false) @ColumnDefault("0")private int pyeong;
    @Column(length = 50) private String tid;
    @Column private LocalDateTime paidAt;
    @Column private LocalDateTime doneAt;
    @Column(nullable = false) @ColumnDefault("0") private int totalPrice;
}
