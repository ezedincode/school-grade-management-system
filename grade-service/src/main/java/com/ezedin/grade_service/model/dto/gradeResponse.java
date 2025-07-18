package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.GradeTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class gradeResponse {
    private GradeTitle gradeTitle;
    private Float score;
}
