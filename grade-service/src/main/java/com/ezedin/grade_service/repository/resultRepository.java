package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.CourseResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface resultRepository extends JpaRepository<CourseResult,Long> {
    CourseResult findByStudentId(Long studentId);
}
