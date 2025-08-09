package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.CourseResult;
import com.ezedin.grade_service.model.enums.courseCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface resultRepository extends JpaRepository<CourseResult,Long> {
    CourseResult findByStudentId(Long studentId);

    List<CourseResult> findAllByStudentId(Long studentId);

    CourseResult findByCourseCode(courseCode courseCode);
}
