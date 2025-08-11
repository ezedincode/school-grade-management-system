package com.ezedin.grade_service.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@ConfigurationProperties(prefix ="courses")
@Getter
@Component
public class courses {
    private List<String> oneToFour;
    private List<String> fiveToSix;
    private List<String> sevenToEight;
}

