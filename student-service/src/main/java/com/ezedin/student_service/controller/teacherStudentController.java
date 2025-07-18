package com.ezedin.student_service.controller;

import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student/teacher")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('teacher')")
public class teacherStudentController {

}
