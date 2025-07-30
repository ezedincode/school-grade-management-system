package com.ezedin.auth_service.controller;

import com.ezedin.auth_service.exception.MissingRequiredFieldsException;
import com.ezedin.auth_service.exception.UserNameExistException;
import com.ezedin.auth_service.model.dto.authenticationRequest;
import com.ezedin.auth_service.model.dto.authenticationResponse;
import com.ezedin.auth_service.model.dto.studentRegistrationRequest;
import com.ezedin.auth_service.model.dto.teacherRegistrationRequest;
import com.ezedin.auth_service.service.authService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class authController {
    private final authService service;

    @PostMapping("/signup/student")
   public ResponseEntity <Map<String,String>> registerStudent(@RequestBody studentRegistrationRequest student) {
        authenticationResponse response;
        try {
            response = service.registerStudent(student);
        } catch (UserNameExistException | MissingRequiredFieldsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("accessToken", response.getAccessToken(),"refreshToken", response.getRefreshToken()));
    }
    @PostMapping("/signup/teacher")
    public ResponseEntity <Map<String,String>> registerTeacher(@RequestBody teacherRegistrationRequest teacher) {
        authenticationResponse response;
        try {
            response = service.registerTeacher(teacher);
        } catch (UserNameExistException | MissingRequiredFieldsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("accessToken", response.getAccessToken(),"refreshToken", response.getRefreshToken()));
    }
    @GetMapping("/login/user")
    public ResponseEntity <Map<String,String>> authenticate(@RequestBody authenticationRequest user) {
        String response=service.authenticate(user);
        if(response.contains("Error")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", response));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Token", response));
    }




}
