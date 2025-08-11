package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.courseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class courseMark {
    private courseCode courseCode;
    private String mark;
}
