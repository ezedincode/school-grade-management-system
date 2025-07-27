package com.ezedin.auth_service.model;

import com.ezedin.auth_service.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String UserName;

    @Column(nullable = false)
    private String Password;
}
