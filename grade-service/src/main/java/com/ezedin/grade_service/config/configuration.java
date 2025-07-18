package com.ezedin.grade_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class configuration {

    @Bean
    public WebClient studentWebClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8080") // Replace with Student Service base URL
                .build();
    }
    @Bean
    public WebClient teacherWebClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8082") // Replace with Student Service base URL
                .build();
    }
}
