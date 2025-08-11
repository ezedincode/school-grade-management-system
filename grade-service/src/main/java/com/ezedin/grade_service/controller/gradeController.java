package com.ezedin.grade_service.controller;

import com.ezedin.grade_service.exception.IncompleteGradesException;
import com.ezedin.grade_service.model.dto.*;
import com.ezedin.grade_service.service.gradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/grade")
//@PriAuthorize("hasRole('Teacher')")
@RequiredArgsConstructor
public class gradeController {
    private final gradeService service;

    @PostMapping("")
    public gradeResponse createGrade(@RequestBody gradeRequest grade) {
        gradeResponse createdGrade;
        try {
            createdGrade = service.createGrade(grade);
        } catch (Exception e) {
            return gradeResponse.builder()
                    .build();
        }
        return createdGrade;
    }
    @GetMapping("/total")
    public String getTotal(@RequestBody markRequest request) {
        try{
        return service.calculateFinalGrade(request.getStudentId(),request.getCourseCode());
    }catch (IncompleteGradesException e){
        return e.getMessage();
        }

    }
    @GetMapping("/termTotal")
    public String getTermTotal(@RequestBody termGradeRequest request) {
        try{
            return service.calculateTermResult(request).toString();
        }catch (IncompleteGradesException e){
            return e.getMessage();
        }

    }
    @GetMapping("/gradeReport")
    public List<gradeReportResponse> getGradeReport(@RequestBody  gradeReportRequest request) {
        return service.gradeReport(request);

    }

}
