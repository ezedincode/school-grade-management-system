package com.ezedin.grade_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TermResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String termId;


    @Column(nullable = false)
    private Float totalScore;

    @Column(nullable = false)
    private Float average;

    @Column(nullable = false)
    private Integer coursesCount;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;
}