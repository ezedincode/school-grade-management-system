package com.ezedin.student_service.service;

import com.ezedin.student_service.model.Dto.studentRequest;
import com.ezedin.student_service.model.Dto.studentResponse;
import com.ezedin.student_service.model.Student;
import com.ezedin.student_service.repository.studentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class studentService {

    private final studentRepository repository;

    private Student mapToDto (studentRequest studentRequest) {
        return Student.builder()
                .age(studentRequest.getAge())
                .name(studentRequest.getName())
                .gender(studentRequest.getGender())
                .grade(studentRequest.getGrade())
                .role(studentRequest.getRole())
                .phone_no(studentRequest.getPhone_no())
                .section(studentRequest.getSection())
                .password(studentRequest.getPassword())
                .build();
    }
    private studentResponse mapToStudent (Student student) {
        return studentResponse.builder()
                .age(student.getAge())
                .name(student.getName())
                .gender(student.getGender())
                .grade(student.getGrade())
                .role(student.getRole())
                .phone_no(student.getPhone_no())
                .section(student.getSection())
                .build();
    }


    public studentResponse createStudent (studentRequest studentRequest) {

        Student student = mapToDto(studentRequest);
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
}
