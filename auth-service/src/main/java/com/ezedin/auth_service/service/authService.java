package com.ezedin.auth_service.service;

import com.ezedin.auth_service.exception.MissingRequiredFieldsException;
import com.ezedin.auth_service.model.User;
import com.ezedin.auth_service.model.dto.*;
import com.ezedin.auth_service.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.ezedin.auth_service.exception.UserNameExistException;
import com.ezedin.auth_service.model.dto.authenticationResponse;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class authService {
    private final userRepository repository;
    private final KafkaTemplate <String,studentRegisteredEvent> kafkaTemplate;
    private final KafkaTemplate <String,teacherRegisteredEvent> teacherkafkaTemplate;
    private final jwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public  String registerStudent(studentRegistrationRequest student){

        try {
            authenticationResponse saveUser= saveUser(student);
            studentRegisteredEvent event = getStudentRegisteredEvent(student,saveUser);
            kafkaTemplate.send("student-registration-topic",event);
            return saveUser.getToken();

        }catch (UserNameExistException | MissingRequiredFieldsException e){
            return  e.getMessage();
        }

    }
    public String registerTeacher(teacherRegistrationRequest teacher){

        try {
            authenticationResponse savedUser= saveUser(teacher);
            teacherRegisteredEvent event = getTeacherRegisteredEvent(teacher,savedUser);
            log.info("check GradeSection {}",event.getGradeSections());
            teacherkafkaTemplate.send("teacher-registration-topic",event);
            return savedUser.getToken();

        }catch (UserNameExistException | MissingRequiredFieldsException e){
            return  e.getMessage();
        }
    }

    private static studentRegisteredEvent getStudentRegisteredEvent(studentRegistrationRequest student,authenticationResponse user) {
        studentRegisteredEvent event =new studentRegisteredEvent();
        event.setRole(student.getRole());
        event.setAge(student.getAge());
        event.setName(student.getName());
        event.setGrade(student.getGrade());
        event.setGender(student.getGender());
        event.setPhone_no(student.getPhone_no());
        event.setSection(student.getSection());
        event.setStudentId(user.getUser().getUserId());
        return event;
    }
    private static teacherRegisteredEvent getTeacherRegisteredEvent(teacherRegistrationRequest teacher, authenticationResponse user) {
        teacherRegisteredEvent event =new teacherRegisteredEvent();
        event.setRole(teacher.getRole());
        event.setName(teacher.getName());
        event.setPhone_no(teacher.getPhone_no());
        event.setTeacherId(user.getUser().getUserId());
        List<GradeSection> gradeSectionDTOs = teacher.getGradeSections();
        event.setGradeSections(gradeSectionDTOs);
        log.info("userTeacherId {}",user.getUser().getUserId());
        log.info("gradeSection {}",event.getGradeSections());
        return event;
    }

    public authenticationResponse mapToResponse(User user) {
        return authenticationResponse .builder()
                 .Token(jwtService.generateToken(new HashMap<>(),user))
                 .user(user)
                 .build();
    }
    public authenticationResponse saveUser (userRegistrationRequest request) throws UserNameExistException, MissingRequiredFieldsException {
       if(repository.findByUserName(request.getUserName()) != null) {
           throw new UserNameExistException("Error : User name exist");
       }
           if (
                      request.getName() == null
                    || request.getPassword() == null
                    || request.getPhone_no() == null) {
               throw new MissingRequiredFieldsException("Error : Missing required user fields");
           }
               User user = new User();
               user.setUserName(request.getUserName());
               user.setPassword(passwordEncoder.encode(request.getPassword()));
               user.setRole(request.getRole());
               repository.save(user);
               return mapToResponse(user);
           }


}