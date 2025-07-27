package com.ezedin.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ezedin.auth_service.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<User ,Long> {
    User findByUserName(String userName);
}
