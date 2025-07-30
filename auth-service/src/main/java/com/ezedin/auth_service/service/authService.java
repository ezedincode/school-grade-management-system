package com.ezedin.auth_service.service;

import com.ezedin.auth_service.exception.MissingRequiredFieldsException;
import com.ezedin.auth_service.model.User;
import com.ezedin.auth_service.model.dto.*;
import com.ezedin.auth_service.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.ezedin.auth_service.exception.UserNameExistException;
import com.ezedin.auth_service.model.dto.authenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class authService {
    private final userRepository repository;
    private final KafkaTemplate <String,studentRegisteredEvent> kafkaTemplate;
    private final KafkaTemplate <String,teacherRegisteredEvent> teacherkafkaTemplate;
    private final jwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final redisService redisService;


    public  authenticationResponse registerStudent(studentRegistrationRequest student)
            throws MissingRequiredFieldsException, UserNameExistException {


            authenticationResponse saveUser= saveUser(student);
            studentRegisteredEvent event = getStudentRegisteredEvent(student,saveUser);
            kafkaTemplate.send("student-registration-topic",event);
            return saveUser;

    }
    public authenticationResponse registerTeacher(teacherRegistrationRequest teacher)
            throws MissingRequiredFieldsException, UserNameExistException {


            authenticationResponse savedUser= saveUser(teacher);
            teacherRegisteredEvent event = getTeacherRegisteredEvent(teacher,savedUser);
            log.info("check GradeSection {}",event.getGradeSections());
            teacherkafkaTemplate.send("teacher-registration-topic",event);
            return savedUser;

    }
    public authenticationResponse authenticate (authenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUserName(),
                request.getPassword()
        ));
        User user = repository.findByUserName(request.getUserName());
        return mapToResponse(user);
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
    @Value("${Access_Token_Expiration_Time}")
    private int  ExpirationTime;
    @Value("${Refresh_Token_Expiration_Time}")
    private int  refreshTokenExpirationTime;
    public authenticationResponse mapToResponse(User user) {
       authenticationResponse response= authenticationResponse .builder()
                 .accessToken(jwtService.generateToken(new HashMap<>(),user))
                 .refreshToken(jwtService.generateRefreshToken())
                 .user(user)
                 .build();

        redisService.storeAccessToken(jwtService.extractJti(response.getAccessToken()),user.getUserId().toString(),ExpirationTime);
        redisService.storeRefreshToken(user.getUserId().toString(),response.getRefreshToken(),refreshTokenExpirationTime);
        return response;
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


    public UserDetails getUserByUserName(String userName) {
        return repository.findByUserName(userName);
    }
}