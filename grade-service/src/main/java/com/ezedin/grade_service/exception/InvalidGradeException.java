package com.ezedin.grade_service.exception;

import com.ezedin.grade_service.model.enums.AssessmentType;

public class InvalidGradeException extends Exception{
    public InvalidGradeException(String message) {
        super(message);
    }
}
