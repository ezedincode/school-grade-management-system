package com.ezedin.teacher_service.model;

import com.ezedin.teacher_service.model.enums.GradeName;
import com.ezedin.teacher_service.model.enums.SectionName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GradeName grade;

    @Enumerated(EnumType.STRING)
    private SectionName section;

    @ManyToOne
    @JoinColumn(name ="teacher_id")
    @JsonBackReference
    private Teacher teacher;



}
