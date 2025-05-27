package com.ssafy.smartstore.dto;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "date"}))
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;

    private LocalDate date;

    public Attendance() {}

    public Attendance(String userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }

    public Integer getId() { return id; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }
}