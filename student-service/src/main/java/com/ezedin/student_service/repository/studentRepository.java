package com.ezedin.student_service.repository;

import com.ezedin.student_service.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface studentRepository extends JpaRepository<Student,Long> {
    Student findByName(String name);
}
