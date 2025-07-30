package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class authenticationResponse {
    private String accessToken;
    private String refreshToken;
    private User user;
}
