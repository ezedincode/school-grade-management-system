package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.GradeTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class gradeResponse {
    private GradeTitle gradeTitle;
    private Float score;
}
