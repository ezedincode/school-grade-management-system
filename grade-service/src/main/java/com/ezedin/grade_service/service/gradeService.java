package com.ezedin.grade_service.service;

import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.dto.gradeRequest;
import com.ezedin.grade_service.model.dto.gradeResponse;
import com.ezedin.grade_service.model.dto.studentResponse;
import com.ezedin.grade_service.model.dto.teacherResponse;
import com.ezedin.grade_service.repository.gradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class gradeService {
    private final gradeRepository repository;
    private final WebClient studentWebClient;
    private final WebClient teacherWebClient;

    private Grade mapToDto (gradeRequest gradeRequest) {
        return Grade.builder()
                .studentId(gradeRequest.getStudentId())
                .teacherID(gradeRequest.getTeacherID())
                .courseTitle(gradeRequest.getCourseTitle())
                .score(gradeRequest.getScore())
                .gradeTitle(gradeRequest.getGradeTitle())
                .build();

    }
    private gradeResponse mapToGrade (Grade grade) {
        return gradeResponse.builder()
                .gradeTitle(grade.getGradeTitle())
                .score(grade.getScore())
                .build();
    }
    public studentResponse getStudent(Long studentId) {
        return studentWebClient.get()
                .uri("/api/student/admin/id/{id}", studentId)
                .retrieve()
                .bodyToMono(studentResponse.class)
                .block();
    }
    public teacherResponse getTeacher(Long teacherId) {
        return teacherWebClient.get()
                .uri("/api/teacher/{id}", teacherId)
                .retrieve()
                .bodyToMono(teacherResponse.class)
                .block();
    }

        public gradeResponse createGrade (gradeRequest request) {
        Grade grade = mapToDto(request);
        studentResponse student = getStudent(grade.getStudentId());
//        teacherResponse teacher = getTeacher(grade.getTeacherID());
//        if(student.getGrade() == teacher.getGrade()
//                && student.getSection() == teacher.getSection()
//                && grade.getCourseTitle() != null
//                && grade.getScore() != null )
//        {
//            repository.save(grade);
//        }
            log.info("student: {}", student);
            repository.save(grade);
        return mapToGrade(grade);

        }

    }


