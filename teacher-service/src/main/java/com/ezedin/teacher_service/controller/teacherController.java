package com.ezedin.teacher_service.controller;

import com.ezedin.teacher_service.model.GradeSection;
import com.ezedin.teacher_service.model.dto.gradeSectionResponse;
import com.ezedin.teacher_service.model.dto.studentResponse;
import com.ezedin.teacher_service.model.dto.teacherRequest;
import com.ezedin.teacher_service.model.dto.teacherResponse;
import com.ezedin.teacher_service.model.enums.GradeName;
import com.ezedin.teacher_service.model.enums.SectionName;
import com.ezedin.teacher_service.service.teacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher")
public class teacherController {

    private final teacherService service;
    @PostMapping("/create")
    public teacherResponse create (@RequestBody teacherRequest request){
        return service.create(request);
    }
    @GetMapping("/{id}")
    public List<gradeSectionResponse> getGradeSectionByTeacherId (@PathVariable("id") Long id){
        return service.getGradeSectionByTeacherId(id);
    }
//    @GetMapping("/students/{grade}/{section}")
//    public List<studentResponse> getStudents (
//            @PathVariable("grade") GradeName grade,
//            @PathVariable("section")SectionName section
//    ){
//
//    }
}
