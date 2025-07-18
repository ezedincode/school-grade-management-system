//package com.ezedin.student_service.model;
//
//import com.ezedin.student_service.model.enums.GradeName;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//@Table
//public class Grade {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Enumerated(EnumType.STRING)
//    private GradeName name;
//
//    @OneToMany
//    private List<Student> students;
//
//
//
//}
