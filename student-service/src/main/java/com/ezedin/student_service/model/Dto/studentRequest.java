package com.ezedin.student_service.model.Dto;

//import com.ezedin.student_service.model.Grade;
//import com.ezedin.student_service.model.Section;
import com.ezedin.student_service.model.enums.Gender;
import com.ezedin.student_service.model.enums.GradeName;
import com.ezedin.student_service.model.enums.Role;
import com.ezedin.student_service.model.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class studentRequest {
    private String name;
    private int age;
    private Gender gender;
    private String phone_no;
    private SectionName section;
    private GradeName grade;
    private Role role;
    private String password;
}
