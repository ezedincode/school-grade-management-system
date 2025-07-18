package com.ezedin.student_service.controller;

import com.ezedin.student_service.model.Dto.studentRequest;
import com.ezedin.student_service.model.Dto.studentResponse;
import com.ezedin.student_service.service.studentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/student/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('Admin')")
public class adminStudentController {

    private final studentService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public studentResponse createStudent (@RequestBody studentRequest student) {
        return service.createStudent(student);
    }

    @PostMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public studentResponse removeStudent (@PathVariable("id") Long id) {
        return service.RemoveStudent(id);
    }

    @GetMapping("/students")
    @ResponseStatus(HttpStatus.OK)
    public List<studentResponse> getAllStudent () {
        return service.getAllStudent();
    }

    @GetMapping("/student/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public studentResponse getStudentById (@PathVariable("id") Long id) {
        return service.getStudentById(id);
    }
    @GetMapping("/student/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public studentResponse getStudentByName (@PathVariable("name") String name) {
        return service.getStudentByName(name);
    }

}
