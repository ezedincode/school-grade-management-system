package com.ezedin.auth_service.service;

import com.ezedin.auth_service.exception.InvalidTokenException;
import com.ezedin.auth_service.exception.MissingRequiredFieldsException;
import com.ezedin.auth_service.model.User;
import com.ezedin.auth_service.model.dto.*;
import com.ezedin.auth_service.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public void logOut (String authHeader){
        String token = authHeader.substring(7);
        String username = jwtService.extractUserId(token);
        User user =repository.findByUserName(username);
        Long userId=0L;
        if(user != null){
             userId =user.getUserId();
        }
        log.info("logged out {}",userId);
        String jti= jwtService.extractJti(token);
        redisService.revokeAccessToken(jti);
        redisService.revokeRefreshToken(userId.toString());
        String refreshToken= redisService.getRefreshToken(userId.toString());
        String sessionId = refreshToken.split(":")[0];
        redisService.revokeSessionId(sessionId);
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
    private Long  ExpirationTime;
    @Value("${Refresh_Token_Expiration_Time}")
    private Long  refreshTokenExpirationTime;
    public authenticationResponse mapToResponse(User user) {
        String sessionId =jwtService.generateSessionId();
       authenticationResponse response= authenticationResponse .builder()
                 .accessToken(jwtService.generateToken(new HashMap<>(),user))
                 .refreshToken(jwtService.generateRefreshToken(sessionId))
                 .user(user)
                 .build();

        redisService.storeAccessToken(jwtService.extractJti(response.getAccessToken()),user.getUserId().toString(),ExpirationTime);
        redisService.storeRefreshToken(user.getUserId().toString(),response.getRefreshToken(),refreshTokenExpirationTime);
        redisService.storeSessionId(sessionId,user.getUserId().toString(),refreshTokenExpirationTime);
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

    public authenticationResponse refresh(Token refreshToken) throws InvalidTokenException {



        String sessionId = refreshToken.getToken().split(":")[0];
        String userIdStr = redisService.getUserId(sessionId);
        if (userIdStr == null) {
            throw new InvalidTokenException("User ID not found for refresh token");
        }

        Long userId;
        try {
            userId = Long.valueOf(userIdStr);
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("Invalid user ID format");
        }
        if (!redisService.isRefreshTokenValid(userId.toString()) {
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }

        User user = repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        redisService.revokeAccessToken(sessionId);
        String newAccessToken = jwtService.generateToken(new HashMap<>(), user);

        return authenticationResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

}