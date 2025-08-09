package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.TermResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface termResultRepository extends JpaRepository<TermResult, Long> {
    Optional<Object> findByStudentId(Long studentId);
}
