package com.ezedin.teacher_service.model.dto;

import com.ezedin.teacher_service.model.enums.Gender;
import com.ezedin.teacher_service.model.enums.GradeName;
import com.ezedin.teacher_service.model.enums.Role;
import com.ezedin.teacher_service.model.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class studentResponse {
    private String name;
    private int age;
    private Gender gender;
    private String phone_no;
    private SectionName section;
    private GradeName grade;
    private Role role;
}
