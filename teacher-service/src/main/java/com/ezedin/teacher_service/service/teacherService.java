package com.ezedin.teacher_service.service;

import com.ezedin.teacher_service.model.GradeSection;
import com.ezedin.teacher_service.model.Teacher;
import com.ezedin.teacher_service.model.dto.*;
import com.ezedin.teacher_service.model.enums.GradeName;
import com.ezedin.teacher_service.model.enums.SectionName;
import com.ezedin.teacher_service.repository.gradeSectionRepository;
import com.ezedin.teacher_service.repository.teacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class teacherService {

    private final teacherRepository TeacherRepository;
    private final gradeSectionRepository GradeSectionRepository;
    private final WebClient StudentWebClient;

    private teacherResponse mapToResponse(Teacher teacher) {
        return teacherResponse.builder()
                .name(teacher.getName())
                .gradeSections(teacher.getGradeSections())
                .phoneNumber(teacher.getPhoneNumber())
                .role(teacher.getRole())
                .build();
    }
    private teacherRequest mapToRequest (Teacher teacher) {
        return teacherRequest.builder()
                .name(teacher.getName())
                .gradeSections(teacher.getGradeSections())
                .phone_no(teacher.getPhoneNumber())
                .role(teacher.getRole())
                .teacherId(teacher.getId())
                .build();
    }

    private gradeSectionResponse mapToGradeSection (GradeSection gradeSection) {
        return gradeSectionResponse.builder()
                .grade(gradeSection.getGrade())
                .section(gradeSection.getSection())
                .build();
    }

    private List<studentResponse> getStudentByGradeAndSection (GradeName grade, SectionName section) {
        return StudentWebClient.get()
                .uri("api/student/admin/{grade}/{section}")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<studentResponse>>() {})
                .block();

    }


    public teacherResponse createTeacher (Teacher teacher) {

        TeacherRepository.save(teacher);
        return mapToResponse(teacher);
    }
    public List<gradeSectionResponse> getGradeSectionByTeacherId (Long id) {
           return GradeSectionRepository.findAllByTeacherId(id)
                   .stream()
                   .map(this::mapToGradeSection)
                   .toList();
    }
    public List<studentResponse> getStudents (GradeName grade, SectionName section){
        return getStudentByGradeAndSection(grade, section);
    }
    @KafkaListener(topics = "teacher-registration-topic", groupId = "teacher-group")
    public void handleStudentRegistration(teacherRegisteredEvent event) {
        Teacher teacher = new Teacher();

        teacher.setName(event.getName());
        teacher.setPhoneNumber(event.getPhone_no());
        teacher.setRole(event.getRole());
        teacher.setId(event.getTeacherId());
        teacher.setGradeSections(event.getGradeSections());
        //log.info("check {}", teacher.getGradeSections());
        if (teacher.getGradeSections() != null){
            for(GradeSection gradeSections :event.getGradeSections()){
                teacher.addGradeSection(gradeSections);
            }
        }
        createTeacher(teacher);
       //log.info("created user {}",createTeacher(teacher));

    }

    }

