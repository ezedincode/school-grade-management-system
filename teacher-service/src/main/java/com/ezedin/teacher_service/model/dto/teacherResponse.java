package com.ezedin.teacher_service.model.dto;

import com.ezedin.teacher_service.model.GradeSection;
import com.ezedin.teacher_service.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class teacherResponse {
    private String name;

    private String phoneNumber;

    private Role role;

    private List<GradeSection> gradeSections;
}
