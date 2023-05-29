package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findOneByUserNameAndPassword(String userName, String Password);
    User findByUserName(String userName);
}
