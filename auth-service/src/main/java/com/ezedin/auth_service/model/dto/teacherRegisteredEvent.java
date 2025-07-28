package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.enums.GradeName;
import com.ezedin.auth_service.model.enums.Role;
import com.ezedin.auth_service.model.enums.SectionName;

public class teacherRegisteredEvent {
    private Long teacherId;
    private String name;

    private String phoneNumber;

    private Role role;
    private GradeName grade;

    private SectionName section;
}
