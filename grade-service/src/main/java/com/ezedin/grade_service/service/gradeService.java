package com.ezedin.grade_service.service;

import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.dto.gradeRequest;
import com.ezedin.grade_service.model.dto.gradeResponse;
import com.ezedin.grade_service.model.dto.studentResponse;
import com.ezedin.grade_service.model.dto.teacherResponse;
import com.ezedin.grade_service.repository.gradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
    public List<teacherResponse> getTeacher(Long teacherId) {

        return teacherWebClient.get()
                .uri("/api/teacher/{id}", teacherId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<teacherResponse>>() {})
                .block();
    }

        public gradeResponse createGrade (gradeRequest request) {
        Grade grade = mapToDto(request);
        studentResponse student = getStudent(grade.getStudentId());
        List<teacherResponse> teachers = getTeacher(grade.getTeacherID());
        boolean foundGradeMatch =teachers.stream()
                .anyMatch(e ->e.getGrade().equals(student.getGrade()));
        boolean foundSectionMatch =teachers.stream()
                    .anyMatch(e ->e.getSection().equals(student.getSection()));
        if(foundGradeMatch && foundSectionMatch
                && grade.getCourseTitle() != null
                && grade.getScore() != null )
        {
            repository.save(grade);
        }
            log.info("student: {}", student.getGrade());
            log.info("teacher: {}", teachers);

        return mapToGrade(grade);

        }

    }


