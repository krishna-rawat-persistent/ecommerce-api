package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findOneByEmailAndPassword(String email, String Password);
    User findByEmail(String Email);
}
