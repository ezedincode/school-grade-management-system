//package com.ezedin.student_service.model;
//
//import com.ezedin.student_service.model.enums.SectionName;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//public class Section {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Enumerated(EnumType.STRING)
//    private SectionName name;
//
//    @OneToMany
//    private List<Student> students;
//
//}
