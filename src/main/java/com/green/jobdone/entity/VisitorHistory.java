package com.green.jobdone.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "visitor_history")
public class VisitorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate visitDate;
    private int visitorCount;

    public VisitorHistory(LocalDate visitDate, int visitorCount) {
        this.visitDate = visitDate;
        this.visitorCount = visitorCount;
    }

    public VisitorHistory() {

    }
}
