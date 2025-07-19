package com.ezedin.teacher_service.model.dto;

import com.ezedin.teacher_service.model.enums.GradeName;
import com.ezedin.teacher_service.model.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class gradeSectionResponse {
    private GradeName grade;

    private SectionName section;
}
