package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.courseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class markRequest {
    private Long studentId;
    private Long teacherId;
    private courseCode courseCode;
}
