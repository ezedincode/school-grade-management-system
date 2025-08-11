package com.ezedin.student_service.service;

import com.ezedin.student_service.config.courses;
import com.ezedin.student_service.model.Course;
import com.ezedin.student_service.model.Dto.studentRegisteredEvent;
import com.ezedin.student_service.model.Dto.studentRequest;
import com.ezedin.student_service.model.Dto.studentResponse;
import com.ezedin.student_service.model.Student;
import com.ezedin.student_service.model.enums.GradeName;
import com.ezedin.student_service.model.enums.SectionName;
import com.ezedin.student_service.repository.studentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class studentService {

    private final studentRepository repository;
    private final courses courses;

    private Student mapToDto (studentRequest studentRequest) {
        return Student.builder()
                .age(studentRequest.getAge())
                .name(studentRequest.getName())
                .gender(studentRequest.getGender())
                .grade(studentRequest.getGrade())
                .role(studentRequest.getRole())
                .phone_no(studentRequest.getPhone_no())
                .section(studentRequest.getSection())
                .id(studentRequest.getId())
                .build();
    }
    private studentResponse mapToStudent (Student student) {
        return studentResponse.builder()
                .id(student.getId())
                .age(student.getAge())
                .name(student.getName())
                .gender(student.getGender())
                .grade(student.getGrade())
                .role(student.getRole())
                .phone_no(student.getPhone_no())
                .section(student.getSection())
                .courses(student.getCourses())
                .build();
    }
    private studentRequest mapToRequest (Student student) {
        return studentRequest.builder()
                .age(student.getAge())
                .name(student.getName())
                .gender(student.getGender())
                .grade(student.getGrade())
                .role(student.getRole())
                .phone_no(student.getPhone_no())
                .section(student.getSection())
                .courses(student.getCourses())
                .id(student.getId())
                .build();
    }

    @KafkaListener(topics = "student-registration-topic", groupId = "student-group")
    public void handleStudentRegistration(studentRegisteredEvent event) {
        Student student = new Student();

        student.setName(event.getName());
        student.setAge(event.getAge());
        student.setGender(event.getGender());
        student.setGrade(event.getGrade());
        student.setPhone_no(event.getPhone_no());
        student.setSection(event.getSection());
        student.setRole(event.getRole());
        student.setId(event.getStudentId());
        createStudent(mapToRequest(student));

    }

    public studentResponse createStudent (studentRequest studentRequest) {

        Student student = mapToDto(studentRequest);
        List<String> defaultCourses;

        GradeName grade = studentRequest.getGrade();

        if (EnumSet.of(GradeName.Grade_1, GradeName.Grade_2, GradeName.Grade_3, GradeName.Grade_4).contains(grade)) {
            defaultCourses = courses.getOneToFour();
        } else if (EnumSet.of(GradeName.Grade_5, GradeName.Grade_6).contains(grade)) {
            defaultCourses = courses.getFiveToSix();
        } else if (EnumSet.of(GradeName.Grade_7, GradeName.Grade_8).contains(grade)) {
            defaultCourses = courses.getSevenToEight();
        } else {
            defaultCourses = List.of();
        }


        for (String title : defaultCourses) {
            Course course = new Course();
            course.setTitle(title);
            student.addCourse(course);
        }
log.info("userId {}",student.getId());
        return mapToStudent(repository.save(student));
    }
    public List<studentResponse> getAllStudent () {
        return repository.findAll()
                .stream()
                .map(this::mapToStudent)
                .collect(Collectors.toList());
    }
    public studentResponse RemoveStudent (Long id) {
            Student student = repository.findById(id).orElse(null);
            if (student != null) {
                repository.delete(student);
                return mapToStudent(student);
            }
           return null;
    }
    public studentResponse getStudentById (Long id) {
        Student student = repository.findById(id).orElse(null);
        if(student != null) {
            return mapToStudent(student);
        }
        return new studentResponse();
    }
    public studentResponse getStudentByName (String name) {
        Student student = repository.findByName(name);
        if(student != null) {
            return mapToStudent(student);
        }
        return new studentResponse();
    }
    public List<studentResponse> getStudentByGradeAndSection(GradeName grade, SectionName section) {
        return repository.findAllByGradeAndSection(grade.name(),section.name())
                .stream()
                .map(this::mapToStudent)
                .collect(Collectors.toList());
    }

}
