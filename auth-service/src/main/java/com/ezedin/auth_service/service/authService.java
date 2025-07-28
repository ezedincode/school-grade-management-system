package com.ezedin.auth_service.service;

import com.ezedin.auth_service.model.User;
import com.ezedin.auth_service.model.dto.studentRegisteredEvent;
import com.ezedin.auth_service.model.dto.studentRegistrationRequest;
import com.ezedin.auth_service.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.ezedin.auth_service.exception.UserNameExistException;


import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class authService {
    private final userRepository repository;
    private final KafkaTemplate <String,studentRegisteredEvent> kafkaTemplate;


    public ResponseEntity<Map<String,String>> registerStudent(studentRegistrationRequest student){

        try {
            User Saveduser= saveUser(student);
            studentRegisteredEvent event = getStudentRegisteredEvent(student,Saveduser);
            kafkaTemplate.send("student-registration-topic",event);

        }catch (UserNameExistException e){
            return  ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status","Error","message",e.getMessage()));
        }catch (IllegalArgumentException e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status","Error","message", e.getMessage()));
        }
        return  ResponseEntity.ok(Map.of("status","Success","message","User registered"));
    }

    private static studentRegisteredEvent getStudentRegisteredEvent(studentRegistrationRequest student,User user) {
        studentRegisteredEvent event =new studentRegisteredEvent();
        event.setRole(student.getRole());
        event.setAge(student.getAge());
        event.setName(student.getName());
        event.setGrade(student.getGrade());
        event.setGender(student.getGender());
        event.setPhone_no(student.getPhone_no());
        event.setSection(student.getSection());
        event.setPassword(student.getPassword());
        event.setStudentId(user.getUserId());
        log.info("userId {}",user.getUserId());
        return event;
    }

    public User saveUser (studentRegistrationRequest request) throws UserNameExistException {
       if(repository.findByUserName(request.getUserName()) != null) {
           throw new UserNameExistException("User name exist");
       }
           if (
                    request.getAge() == 0
                    || request.getName() == null
                    || request.getSection() == null
                    || request.getPassword() == null
                    || request.getPhone_no() == null) {
               throw new IllegalArgumentException("Missing required user fields");
           }
               User user = new User();
               user.setUserName(request.getUserName());
               user.setPassword(request.getPassword());
               user.setRole(request.getRole());
               return repository.save(user);
           }

       }