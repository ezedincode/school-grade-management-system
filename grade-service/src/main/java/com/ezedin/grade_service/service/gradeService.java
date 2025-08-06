package com.ezedin.grade_service.service;

import com.ezedin.grade_service.exception.InvalidGradeException;
import com.ezedin.grade_service.exception.UnsupportedAssessmentType;
import com.ezedin.grade_service.model.Assessment;
import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.dto.gradeRequest;
import com.ezedin.grade_service.model.dto.gradeResponse;
import com.ezedin.grade_service.model.dto.studentResponse;
import com.ezedin.grade_service.model.dto.teacherResponse;
import com.ezedin.grade_service.model.enums.courseCode;
import com.ezedin.grade_service.repository.assessmentRepository;
import com.ezedin.grade_service.repository.gradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ezedin.grade_service.model.enums.AssessmentType.FinalExam;

@Service
@RequiredArgsConstructor
@Slf4j
public class gradeService {
    private final gradeRepository gradeRepository;
    private final WebClient studentWebClient;
    private final WebClient teacherWebClient;
    private final assessmentRepository assessmentRepository;

    private Grade mapToDto (gradeRequest gradeRequest) {
        return Grade.builder()
                .studentId(gradeRequest.getStudentId())
                .teacherID(gradeRequest.getTeacherID())
                .score(gradeRequest.getScore())
                .build();

    }
    private gradeResponse mapToGrade (Grade grade) {
        return gradeResponse.builder()
                .score(grade.getScore())
                .assessmentType(grade.getAssessment().getAssessmentType())
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

        public gradeResponse createGrade (gradeRequest request) throws InvalidGradeException {
        studentResponse student = getStudent(request.getStudentId());
        List<teacherResponse> teachers = getTeacher(request.getTeacherID());
        boolean foundGradeMatch =teachers.stream()
                .anyMatch(e ->e.getGrade().equals(student.getGrade()));
        boolean foundSectionMatch =teachers.stream()
                    .anyMatch(e ->e.getSection().equals(student.getSection()));
        if(!(foundGradeMatch && foundSectionMatch && request.getScore() != null ))
        {
            return null;
        }

            Assessment assessment = assessmentRepository
                    .findByCourseCodeAndAssessmentType(request.getCourseCode(), request.getAssessmentType())
                    .orElseGet(() -> createDefaultAssessment(request));

            // 2. Validate score doesn't exceed max
            if (request.getScore() > assessment.getMaxScore()) {
                throw new InvalidGradeException("grade error");
            }
            Grade grade = new Grade();
            grade.setStudentId(request.getStudentId());
            grade.setTeacherID(request.getTeacherID());
            grade.setScore(request.getScore());
            grade.setAssessment(assessment);
            grade = gradeRepository.save(grade);

            log.info("student: {}", student.getGrade());
            log.info("teacher: {}", teachers);

           return mapToGrade(grade);

        }
    private Assessment createDefaultAssessment(gradeRequest request) {
        float maxScore = switch(request.getAssessmentType()) {
            case ClassRoomAssessment -> 10.0f;
            case Assigment -> 15.0f;
            case MidExam -> 25.0f;
            case FinalExam -> 50.0f;
        };

        Assessment assessment = new Assessment();
        assessment.setCourseCode(request.getCourseCode());
        assessment.setAssessmentType(request.getAssessmentType());
        assessment.setMaxScore(maxScore);
        return assessmentRepository.save(assessment);
    }
    public Float calculateFinalGrade(Long studentId, courseCode courseCode) {
        // Get all assessments for the course
        List<Assessment> assessments = assessmentRepository.findByCourseCode(courseCode);

        // Get student's grades for these assessments
        List<Grade> grades = gradeRepository.findByStudentIdAndAssessmentCourseCode(studentId, courseCode);

        // Create grade map for quick lookup
        Map<Long, Grade> gradeMap = grades.stream()
                .collect(Collectors.toMap(g -> g.getAssessment().getId(), Function.identity()));

        // Calculate totals
        float totalPossible = 0;
        float totalEarned = 0;

        for (Assessment a : assessments) {
            totalPossible += a.getMaxScore();

            Grade g = gradeMap.get(a.getId());
            if (g != null) {
                totalEarned += g.getScore();
            }
            // Handle missing grades as 0 if needed
        }

        // Return final percentage (automatically scaled to 100)
        return (totalEarned / totalPossible) * 100;
    }

    }


