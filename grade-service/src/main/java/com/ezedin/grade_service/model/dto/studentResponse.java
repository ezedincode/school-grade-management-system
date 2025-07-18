package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.Gender;
import com.ezedin.grade_service.model.enums.GradeName;
import com.ezedin.grade_service.model.enums.Role;
import com.ezedin.grade_service.model.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class studentResponse {
    private String name;
    private int age;
    private Gender gender;
    private String phone_no;
    private SectionName section;
    private GradeName grade;
    private Role role;
}
