package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.enums.courseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface gradeRepository extends JpaRepository<Grade ,Long> {
    List<Grade> findByStudentIdAndAssessmentCourseCode(Long studentId, courseCode assessment_courseCode);
}
