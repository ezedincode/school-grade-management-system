package com.ezedin.auth_service.controller;

import com.ezedin.auth_service.model.dto.studentRegistrationRequest;
import com.ezedin.auth_service.model.dto.teacherRegistrationRequest;
import com.ezedin.auth_service.service.authService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class authController {
    private final authService service;

    @PostMapping("/signup/student")
   public ResponseEntity <Map<String,String>> registerStudent(@RequestBody studentRegistrationRequest student) {
        String response=service.registerStudent(student);
        if(response.contains("Error")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", response));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Token", response));
 }
    @PostMapping("/signup/teacher")
    public ResponseEntity <Map<String,String>> registerTeacher(@RequestBody teacherRegistrationRequest teacher) {
        String response=service.registerTeacher(teacher);
        if(response.contains("Error")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", response));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Token", response));
    }




}
