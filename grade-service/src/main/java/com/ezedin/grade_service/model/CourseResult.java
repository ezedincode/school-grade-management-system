package com.ezedin.grade_service.model;

import com.ezedin.grade_service.model.enums.courseCode;

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
public class CourseResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)    
    private courseCode courseCode;

    @Column(nullable = false)
    private Float totalScore;


    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}
