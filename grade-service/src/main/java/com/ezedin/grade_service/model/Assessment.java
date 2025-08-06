package com.ezedin.grade_service.model;

import com.ezedin.grade_service.model.enums.AssessmentType;
import jakarta.persistence.*;
import com.ezedin.grade_service.model.enums.courseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private courseCode courseCode;

    private Float maxScore;

    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

}