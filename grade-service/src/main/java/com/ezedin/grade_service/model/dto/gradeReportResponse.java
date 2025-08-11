package com.ezedin.grade_service.model.dto;

import com.ezedin.grade_service.model.enums.GradeName;
import com.ezedin.grade_service.model.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class gradeReportResponse {
    private String studentName;
    private GradeName grade;
    private SectionName section;
    private List<courseMark> courseMark;
    private Float total;
    private String rank;


}
