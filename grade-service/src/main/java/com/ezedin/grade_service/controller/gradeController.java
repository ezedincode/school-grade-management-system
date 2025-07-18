package com.ezedin.grade_service.controller;

import com.ezedin.grade_service.model.dto.gradeRequest;
import com.ezedin.grade_service.model.dto.gradeResponse;
import com.ezedin.grade_service.service.gradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/grade")
//@PriAuthorize("hasRole('Teacher')")
@RequiredArgsConstructor
public class gradeController {
    private final gradeService service;

    @PostMapping("")
    public gradeResponse createGrade(@RequestBody gradeRequest grade) {
        return service.createGrade(grade);
    }
}
