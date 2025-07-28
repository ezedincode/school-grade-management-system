package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.enums.Gender;
import com.ezedin.auth_service.model.enums.GradeName;
import com.ezedin.auth_service.model.enums.Role;
import com.ezedin.auth_service.model.enums.SectionName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class studentRegistrationRequest {
    private String name;
    private int age;
    private Gender gender;
    private String phone_no;
    private SectionName section;
    private GradeName grade;
    private Role role;

    private String password;
    private String userName;
}
