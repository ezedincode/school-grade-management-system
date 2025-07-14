package com.ezedin.student_service.controller;

import com.ezedin.student_service.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/student/admin")
@RequiredArgsConstructor
public class adminStudentController {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent (@RequestBody Student student) {
        return null;
    }

    @PostMapping("remove")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Student removeStudent (@RequestBody Student student) {
        return null;
    }

    @GetMapping("/students")
    @ResponseStatus(HttpStatus.OK)
    public List<Student> getAllStudent () {
        return null;
    }

    @GetMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Student getStudentById (@PathVariable("id") Long id) {
        return null;
    }
    @GetMapping("/student/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Student getStudentByName (@PathVariable("name") Long id) {
        return null;
    }

}
