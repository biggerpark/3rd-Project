package com.green.jobdone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "visitor_count")
public class VisitorCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}