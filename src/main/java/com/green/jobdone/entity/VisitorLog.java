package com.green.jobdone.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visitor_log", uniqueConstraints = @UniqueConstraint(columnNames = {"ipAddress", "visitDate"}))
public class VisitorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private LocalDate visitDate;

    public VisitorLog() {}

    public VisitorLog(String ipAddress, LocalDate visitDate) {
        this.ipAddress = ipAddress;
        this.visitDate = visitDate;
    }
}
