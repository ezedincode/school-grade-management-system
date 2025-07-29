package com.ezedin.teacher_service.model.dto;


import com.ezedin.teacher_service.model.GradeSection;
import com.ezedin.teacher_service.model.enums.GradeName;
import com.ezedin.teacher_service.model.enums.Role;
import com.ezedin.teacher_service.model.enums.SectionName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class teacherRegisteredEvent {
    private Long teacherId;
    private String name;
    private String phone_no;
    private Role role;
   private List<GradeSection> gradeSections;
}
