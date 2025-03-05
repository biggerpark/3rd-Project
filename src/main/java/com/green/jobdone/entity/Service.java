package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@DynamicUpdate
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
    @Column(columnDefinition = "DATETIME(0)") private LocalDateTime paidAt;
    @Column(columnDefinition = "DATETIME(0)") private LocalDateTime doneAt;
    @Column(nullable = false) @ColumnDefault("0") private int totalPrice;

//    @OneToMany(mappedBy = "service_option", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ServiceOption> serviceOptionList = new ArrayList<>();
//
//    public void addServiceOption(ServiceOption serviceOption) {
//        serviceOptionList.add(serviceOption);
//        serviceOption.setService(this);
//    } 양방향이긴한데 이미 적용해둔게 있어서 나중에 생각
}
