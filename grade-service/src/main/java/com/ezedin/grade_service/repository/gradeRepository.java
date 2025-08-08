package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.Assessment;
import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.enums.courseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface gradeRepository extends JpaRepository<Grade ,Long> {
    List<Grade> findByStudentIdAndAssessmentCourseCode(Long studentId, courseCode assessment_courseCode);

    Optional<Grade> findByStudentIdAndTeacherIDAndAssessment(Long studentId, Long teacherID, Assessment assessment);
}
