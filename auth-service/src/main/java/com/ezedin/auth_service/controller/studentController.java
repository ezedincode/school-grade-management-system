package com.ezedin.auth_service.controller;

import com.ezedin.auth_service.model.dto.studentRegisteredEvent;
import com.ezedin.auth_service.model.dto.studentRegistrationRequest;
import com.ezedin.auth_service.service.studentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class studentController {
    private final studentService service;

    @PostMapping("/signup/student")
   public ResponseEntity <Map<String,String>> registerStudent(@RequestBody studentRegistrationRequest student) {
        return service.registerStudent(student);
 }



}
