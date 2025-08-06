package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class gradeResponse {
    private AssessmentType assessmentType;
    private Float score;
}
