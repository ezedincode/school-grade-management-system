package com.ezedin.teacher_service.repository;

import com.ezedin.teacher_service.model.GradeSection;
import com.ezedin.teacher_service.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface gradeSectionRepository extends JpaRepository<GradeSection ,Long> {
    GradeSection findByTeacherId(Long id);
}
