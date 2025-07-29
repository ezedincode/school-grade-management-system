package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class teacherRegisteredEvent {
    private Long teacherId;
    private String name;
    private String phone_no;
    private Role role;
    private List<GradeSection> gradeSections;
}
