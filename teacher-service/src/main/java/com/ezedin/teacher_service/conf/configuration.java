package com.ezedin.teacher_service.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class configuration {
    @Bean
    public WebClient StudentWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/student")
                .build();
    }
}
