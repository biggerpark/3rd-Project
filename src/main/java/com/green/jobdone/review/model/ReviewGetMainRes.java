package com.green.jobdone.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ReviewGetMainRes {
    private String name;
    private String pic;
    private Double score;
    private String contents;
    private LocalDateTime createdAt;

    public ReviewGetMainRes(String name, String pic, Double score, String contents, LocalDateTime createdAt) {
        this.name = name;
        this.pic = pic;
        this.score = score;
        this.contents = contents;
        this.createdAt = createdAt;
    }
}
