package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.GradeName;
import com.ezedin.grade_service.model.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class teacherResponse {
    private SectionName section;
    private GradeName grade;
}
