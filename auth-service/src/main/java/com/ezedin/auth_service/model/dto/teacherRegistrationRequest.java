package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.enums.GradeName;
import com.ezedin.auth_service.model.enums.Role;
import com.ezedin.auth_service.model.enums.SectionName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class teacherRegistrationRequest  implements userRegistrationRequest{


    private String name;
    private String phone_no;
    private Role role;
    private List<GradeSection> gradeSections;
    private String password;
    private String userName;
}
