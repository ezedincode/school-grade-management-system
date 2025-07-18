package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface gradeRepository extends JpaRepository<Grade ,Long> {
}
