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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String Password;
}
