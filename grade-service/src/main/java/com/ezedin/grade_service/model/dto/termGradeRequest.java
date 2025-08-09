package com.ezedin.grade_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class termGradeRequest {
    private Long studentId;
    private Long teacherId;
    private int  noOdCourses;
}
