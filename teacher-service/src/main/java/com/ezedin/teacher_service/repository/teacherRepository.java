package com.ezedin.teacher_service.repository;

import com.ezedin.teacher_service.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface teacherRepository extends JpaRepository<Teacher,Long> {
}
