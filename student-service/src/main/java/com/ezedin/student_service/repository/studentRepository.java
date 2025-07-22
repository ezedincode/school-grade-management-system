package com.ezedin.student_service.repository;

import com.ezedin.student_service.model.Student;
import com.ezedin.student_service.model.enums.GradeName;
import com.ezedin.student_service.model.enums.SectionName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface studentRepository extends JpaRepository<Student,Long> {
    Student findByName(String name);

    //List<Student> getStudentByGradeAndSection(GradeName grade, SectionName section);

    @Query(value = "SELECT * FROM student WHERE grade = :grade AND section = :section", nativeQuery = true)
    List<Student> findAllByGradeAndSection(@Param("grade") String grade, @Param("section") String section);


}
