package com.ezedin.grade_service.exception;

import com.ezedin.grade_service.model.enums.AssessmentType;

public class UnsupportedAssessmentType extends Exception{
    public UnsupportedAssessmentType(AssessmentType assessmentType){
        super("assessmentType " + assessmentType + " is not supported");
    }
}
