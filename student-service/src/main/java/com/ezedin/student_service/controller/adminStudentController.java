package com.ezedin.student_service.controller;

import com.ezedin.student_service.model.Dto.studentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/student/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Admin')")
public class adminStudentController {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public studentResponse createStudent (@RequestBody studentResponse student) {
        return null;
    }

    @PostMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public studentResponse removeStudent (@PathVariable("id") Long id) {
        return null;
    }

    @GetMapping("/students")
    @ResponseStatus(HttpStatus.OK)
    public List<studentResponse> getAllStudent () {
        return null;
    }

    @GetMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public studentResponse getStudentById (@PathVariable("id") Long id) {
        return null;
    }
    @GetMapping("/student/{name}")
    @ResponseStatus(HttpStatus.OK)
    public studentResponse getStudentByName (@PathVariable("name") Long id) {
        return null;
    }

}
