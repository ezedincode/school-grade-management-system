package com.ezedin.teacher_service.controller;

import com.ezedin.teacher_service.model.dto.teacherRequest;
import com.ezedin.teacher_service.model.dto.teacherResponse;
import com.ezedin.teacher_service.service.teacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public teacherResponse getGradeSectionByTeacherId (@PathVariable("id") Long id){
        return service.getGradeSectionByTeacherId(id);
    }
}
