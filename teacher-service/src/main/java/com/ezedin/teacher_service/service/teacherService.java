package com.ezedin.teacher_service.service;

import com.ezedin.teacher_service.model.GradeSection;
import com.ezedin.teacher_service.model.Teacher;
import com.ezedin.teacher_service.model.dto.teacherRequest;
import com.ezedin.teacher_service.model.dto.teacherResponse;
import com.ezedin.teacher_service.repository.gradeSectionRepository;
import com.ezedin.teacher_service.repository.teacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class teacherService {

    private final teacherRepository TeacherRepository;
    private final gradeSectionRepository GradeSectionRepository;

    private Teacher mapToDto (teacherRequest teacherRequest) {
        return Teacher.builder()
                .name(teacherRequest.getName())
                .gradeSections(teacherRequest.getGradeSections())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .role(teacherRequest.getRole())
                .build();
    }
    private teacherResponse mapToTeacher (Teacher teacher) {
        return teacherResponse.builder()
                .name(teacher.getName())
                .gradeSections(teacher.getGradeSections())
                .phoneNumber(teacher.getPhoneNumber())
                .role(teacher.getRole())
                .build();
    }

    public teacherResponse create (teacherRequest teacherRequest) {
        Teacher teacher =mapToDto(teacherRequest);
        if (teacher.getGradeSections() != null){
            for(GradeSection gradeSection :teacher.getGradeSections()){
                teacher.addGradeSection(gradeSection);
            }
        }
        TeacherRepository.save(teacher);
        return mapToTeacher(teacher);
    }
    public teacherResponse getGradeSectionByTeacherId (Long id) {
        GradeSection gradeSection=GradeSectionRepository.findByTeacherId(id);
        Teacher teacher=TeacherRepository.findById(gradeSection.getTeacher().getId()).orElse(new Teacher());

        return mapToTeacher(teacher);
    }

    }

