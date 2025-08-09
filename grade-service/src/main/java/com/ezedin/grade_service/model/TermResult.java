package com.ezedin.grade_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TermResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;




    @Column(nullable = false)
    private Float totalScore;

    @Column(nullable = false)
    private Float average;


    @Column(nullable = false)
    private LocalDateTime calculatedAt;
}