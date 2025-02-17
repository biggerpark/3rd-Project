package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
@Table(name="review_pic")
public class ReviewPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewPicId;

    @ManyToOne
    @JoinColumn(name = "reviewId", nullable = false)
    private Review review;


    @Column(length = 100)
    private String pic;

    @Column
    @ColumnDefault("0")
    private int state;

}
