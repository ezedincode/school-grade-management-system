package com.ezedin.grade_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;


    @Column(nullable = false)
    private Long teacherID;

    @Column(nullable = false)
    private Float score;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;


}
