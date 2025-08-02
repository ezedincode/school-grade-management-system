//package com.ezedin.api_gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//
//@Configuration
//public class corsConfig {
//    @Bean
//    public CorsWebFilter corsFilter() {
//        return new CorsWebFilter(exchange -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowCredentials(true);
//            config.addAllowedOrigin("*");
//            config.addAllowedHeader("*");
//            config.addAllowedMethod("*");
//            return config;
//        });
//    }
//}
