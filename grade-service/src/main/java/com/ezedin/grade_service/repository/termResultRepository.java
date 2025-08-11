package com.ezedin.grade_service.repository;

import com.ezedin.grade_service.model.TermResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface termResultRepository extends JpaRepository<TermResult, Long> {
    Optional<Object> findByStudentId(Long studentId);

    @Query("SELECT t.studentId FROM TermResult t WHERE t.studentId IN :studentIds ORDER BY t.average DESC")
    List<Long> findStudentIdsOrderByAverageDesc(@Param("studentIds") List<Long> studentIds);

    TermResult findTotalScoreByStudentId(Long id);
}
