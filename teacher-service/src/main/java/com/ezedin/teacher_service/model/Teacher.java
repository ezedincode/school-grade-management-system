package com.ezedin.teacher_service.model;

import com.ezedin.teacher_service.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    private Long Id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "teacher" ,cascade = CascadeType.ALL ,orphanRemoval = true)
    @JsonManagedReference
    private List<GradeSection> gradeSections;

    public void addGradeSection(GradeSection gradeSection) {
        gradeSection.setTeacher(this);
    }
}
