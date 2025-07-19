package com.ezedin.teacher_service.repository;

import com.ezedin.teacher_service.model.GradeSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface gradeSectionRepository extends JpaRepository<GradeSection ,Long> {
    GradeSection findByTeacherId(Long id);

    List <GradeSection> findAllByTeacherId(Long id);
}
