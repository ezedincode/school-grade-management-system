package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.enums.GradeName;
import com.ezedin.auth_service.model.enums.SectionName;
import lombok.Data;

@Data
public class GradeSection {
    private GradeName grade;
    private SectionName section;
}
