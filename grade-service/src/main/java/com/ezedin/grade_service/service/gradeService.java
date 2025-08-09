package com.ezedin.grade_service.service;

import com.ezedin.grade_service.exception.IncompleteGradesException;
import com.ezedin.grade_service.exception.InvalidGradeException;
import com.ezedin.grade_service.model.Assessment;
import com.ezedin.grade_service.model.CourseResult;
import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.TermResult;
import com.ezedin.grade_service.model.dto.*;
import com.ezedin.grade_service.model.enums.courseCode;
import com.ezedin.grade_service.repository.assessmentRepository;
import com.ezedin.grade_service.repository.gradeRepository;
import com.ezedin.grade_service.repository.resultRepository;
import com.ezedin.grade_service.repository.termResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class gradeService {
    private final gradeRepository gradeRepository;
    private final WebClient studentWebClient;
    private final WebClient teacherWebClient;
    private final assessmentRepository assessmentRepository;
    private final resultRepository resultRepository;
    private final termResultRepository termResultRepository;

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
    public boolean validateUser(Long studentId, Long teacherId) {
        studentResponse student = getStudent(studentId);
        List<teacherResponse> teachers = getTeacher(teacherId);

        boolean foundGradeMatch = teachers.stream()
                .anyMatch(e -> e.getGrade().equals(student.getGrade()));
        boolean foundSectionMatch = teachers.stream()
                .anyMatch(e -> e.getSection().equals(student.getSection()));

        return foundGradeMatch && foundSectionMatch;
    }

    @Transactional
    public gradeResponse createGrade(gradeRequest request) throws InvalidGradeException {


        if (!(validateUser(request.getStudentId(),request.getTeacherID()) && request.getScore() != null) ){
            return null;
        }

        Assessment assessment = assessmentRepository
                .findByCourseCodeAndAssessmentType(request.getCourseCode(), request.getAssessmentType())
                .orElseGet(() -> createDefaultAssessment(request));

        if (request.getScore() > assessment.getMaxScore()) {
            throw new InvalidGradeException("grade error");
        }

        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndTeacherIDAndAssessment(
                request.getStudentId(),
                request.getTeacherID(),
                assessment
        );


        Grade grade;
        if (existingGrade.isPresent()) {
            // Update existing grade
            grade = existingGrade.get();
            grade.setScore(request.getScore());
        } else {

            grade = new Grade();
            grade.setStudentId(request.getStudentId());
            grade.setTeacherID(request.getTeacherID());
            grade.setScore(request.getScore());
            grade.setAssessment(assessment);
        }
        grade = gradeRepository.save(grade);



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
    public String calculateFinalGrade(Long studentId, courseCode courseCode) throws IncompleteGradesException {
        List<Assessment> assessments = assessmentRepository.findByCourseCode(courseCode);
        List<Grade> grades = gradeRepository.findByStudentIdAndAssessmentCourseCode(studentId, courseCode);
        Map<Long, Grade> gradeMap = grades.stream()
                .collect(Collectors.toMap(g -> g.getAssessment().getId(), Function.identity()));

        List<Assessment> missing = assessments.stream()
                .filter(a -> !gradeMap.containsKey(a.getId()))
                .toList();

        if (!missing.isEmpty()) {
            throw new IncompleteGradesException("Missing grades for: " +
                    missing.stream().map(Assessment::getAssessmentType).toList());

        }
        float totalPossible = 0;
        float totalEarned = 0;


        for (Assessment a : assessments) {
            totalPossible += a.getMaxScore();

            Grade g = gradeMap.get(a.getId());
            if (g != null) {
                totalEarned += g.getScore();
            }
        }
        CourseResult courseResult=resultRepository.findByCourseCode(courseCode);
        if(courseResult == null) {
            saveTotal(studentId,courseCode,totalEarned);
        }

        return "Total : " + totalEarned;
    }
    public void saveTotal (Long studentId,courseCode courseCode ,Float totalMark)  {

            resultRepository.save(CourseResult.builder()
                    .studentId(studentId)
                    .totalScore(totalMark)
                    .courseCode(courseCode)
                    .lastUpdated(LocalDateTime.now())
                    .build());
        }

    @Transactional
    public Float calculateTermResult(termGradeRequest request) throws IncompleteGradesException {

        if (request.getStudentId() == null || request.getNoOdCourses() <= 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        List<CourseResult> results = resultRepository.findAllByStudentId(request.getStudentId());

        if (results.size() != request.getNoOdCourses()) {
            throw new IncompleteGradesException(
                    String.format("Incomplete grades. Expected %d courses, found %d",
                            request.getNoOdCourses(), results.size())
            );
        }

        Float totalMark = results.stream()
                .map(CourseResult::getTotalScore)
                .reduce(0f, Float::sum);

        saveTermTotal(request.getStudentId(), totalMark, request.getNoOdCourses());

        return totalMark;
    }

    @Transactional
    public void saveTermTotal(Long studentId, Float totalMark, int courses) {
        Float average = totalMark / courses;

        TermResult termResult = (TermResult) termResultRepository.findByStudentId(studentId)
                .orElseGet(TermResult::new);

        termResult.setStudentId(studentId);
        termResult.setTotalScore(totalMark);
        termResult.setAverage(average);
        termResult.setCalculatedAt(LocalDateTime.now());

        termResultRepository.save(termResult);
    }
    }


