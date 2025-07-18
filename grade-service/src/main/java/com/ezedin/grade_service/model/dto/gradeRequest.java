package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.GradeTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class gradeRequest {
    private Long studentId;

    private String courseTitle;

    private Long teacherID;

    private GradeTitle gradeTitle;

    private Float score;
}
