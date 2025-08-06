package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.Assessment;
import com.ezedin.grade_service.model.enums.AssessmentType;
import com.ezedin.grade_service.model.enums.courseCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface assessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByCourseCode(courseCode courseCode);

    Optional<Assessment> findByCourseCodeAndAssessmentType(courseCode courseCode, AssessmentType assessmentType);
}
