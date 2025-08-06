package com.ezedin.student_service.model;

import com.ezedin.student_service.model.enums.Gender;
import com.ezedin.student_service.model.enums.GradeName;
import com.ezedin.student_service.model.enums.Role;
import com.ezedin.student_service.model.enums.SectionName;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Student {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String phone_no;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeName grade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SectionName section;

    @OneToMany(mappedBy = "student" ,cascade = CascadeType.ALL ,orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<Course> courses =new ArrayList<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.setStudent(this);
    }

}
