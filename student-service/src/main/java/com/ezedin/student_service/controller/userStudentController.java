package com.ezedin.student_service.controller;

import com.ezedin.student_service.model.Dto.studentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/student/user")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('Student')")
public class userStudentController {


    //session
    @GetMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public studentResponse getStudentById (@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public studentResponse resetPassword (@PathVariable("id") Long id) {
        return null;
    }
}
