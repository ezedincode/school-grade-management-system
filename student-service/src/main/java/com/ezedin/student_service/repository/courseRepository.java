package com.ezedin.student_service.repository;

import com.ezedin.student_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface courseRepository extends JpaRepository<Course,Long> {

}
