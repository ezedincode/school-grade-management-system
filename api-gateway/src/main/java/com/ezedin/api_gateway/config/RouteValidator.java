package com.ezedin.api_gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteValidator {
    public static final List<String> openRequests = List.of(
            "/api/auth/signup/student",
            "/api/auth/login",
            "/api/auth/signup/teacher"
    );
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                boolean isSecured = openRequests.stream()
                        .noneMatch(pattern -> pathMatcher.match(pattern, path));

                log.debug("Path: {} - Secured: {}", path, isSecured);
                return isSecured;
            };
}
