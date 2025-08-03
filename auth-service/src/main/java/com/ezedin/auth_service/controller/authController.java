package com.ezedin.auth_service.controller;

import com.ezedin.auth_service.exception.InvalidTokenException;
import com.ezedin.auth_service.exception.MissingRequiredFieldsException;
import com.ezedin.auth_service.exception.UserNameExistException;
import com.ezedin.auth_service.model.dto.*;
import com.ezedin.auth_service.service.authService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }
        service.logOut(authHeader);
        return ResponseEntity.ok("Logged out successfully");
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
    @PostMapping("/login")
    public ResponseEntity <Map<String,String>> authenticate(@RequestBody authenticationRequest user) {
        authenticationResponse response=service.authenticate(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("accessToken", response.getAccessToken(),"refreshToken", response.getRefreshToken()));
    }
    @PostMapping("/refresh")
    public ResponseEntity <Map<String,String>> refresh(@RequestBody Token refreshToken) {
        authenticationResponse response;
        try {
            response = service.refresh(refreshToken);
        } catch (InvalidTokenException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("accessToken", response.getAccessToken()));
    }




}
