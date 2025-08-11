package com.ezedin.grade_service.service;

import com.ezedin.grade_service.exception.IncompleteGradesException;
import com.ezedin.grade_service.exception.InvalidGradeException;
import com.ezedin.grade_service.model.Assessment;
import com.ezedin.grade_service.model.CourseResult;
import com.ezedin.grade_service.model.Grade;
import com.ezedin.grade_service.model.TermResult;
import com.ezedin.grade_service.config.courses;
import com.ezedin.grade_service.model.dto.*;
import com.ezedin.grade_service.model.enums.GradeName;
import com.ezedin.grade_service.model.enums.SectionName;
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
import java.util.*;
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
    private final courses courses;

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
            log.info("inside if");
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

        log.info("grade created {}", grade);

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

         Assessment assessment1= assessmentRepository.save(assessment);
        log.info("assessment created{} " , assessment);
        return assessment1;
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
        CourseResult courseResult=resultRepository.findByCourseCodeAndStudentId(courseCode,studentId);
        log.info("course result: {}", courseResult);
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
    public List<studentResponse> getStudentByGradeAndSection(GradeName gradeName,SectionName sectionName) {
        return studentWebClient.get()
                .uri("/api/student/admin/{grade}/{section}", gradeName,sectionName)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<studentResponse>>() {})
                .block();
    }

    public List<gradeReportResponse> gradeReport (gradeReportRequest request) {
        List<studentResponse> response =getStudentByGradeAndSection(request.getGrade(),request.getSection());
        List<gradeReportResponse> reports = new ArrayList<>();

        List<Long> studentIds=response.stream().map(studentResponse::getId).toList();
        log.info("studentIds: {}", studentIds);

        Map<String,String> map =rank(studentIds);
        for(studentResponse r : response){
            reports.add(gradeReportResponse.builder()
                            .studentName(r.getName())
                            .grade(r.getGrade())
                            .section(r.getSection())
                            .courseMark(MapToCourseMark(r,request.getGrade(),request.getSection()))
                            .total(termResultRepository.findTotalScoreByStudentId(r.getId()).getTotalScore())
                            .rank(map.get(r.getId().toString()))
                    .build());
        }
        return reports;
    }
    public Map<String,String> rank(List<Long> studentIds) {

        List<Long> Ids =termResultRepository.findStudentIdsOrderByAverageDesc(studentIds);
        Map<String,String> map = new HashMap<>();
        Integer rank = 1;
        for (Long studentId : Ids) {
            map.put(studentId.toString(),rank.toString());
            rank++;
        }

        return  map;
    }
    public List<courseMark> MapToCourseMark(studentResponse response,GradeName gradeName, SectionName sectionName) {
        List<String> Courses;
        List<courseCode> courseCodes = new ArrayList<>();
        if (EnumSet.of(GradeName.Grade_1, GradeName.Grade_2, GradeName.Grade_3, GradeName.Grade_4).contains(gradeName)){
            Courses=courses.getOneToFour().stream().toList();
        }else if (EnumSet.of(GradeName.Grade_5, GradeName.Grade_6).contains(gradeName)) {
            Courses = courses.getFiveToSix().stream().toList();;
        } else if (EnumSet.of(GradeName.Grade_7, GradeName.Grade_8).contains(gradeName)) {
            Courses = courses.getSevenToEight().stream().toList();;
        } else {
            Courses = List.of();
        }
        for(String course:Courses){
            courseCodes.add(courseCode.valueOf(course));
        }
        List<courseMark> courseMarks = new ArrayList<>();
        for (courseCode c  : courseCodes) {
            courseMarks.add(courseMark.builder()
                            .courseCode(c)
                            .mark(resultRepository.findByStudentIdAndCourseCode(response.getId(),c).getTotalScore().toString())
                    .build());
        }
        return courseMarks;
    }

    }


