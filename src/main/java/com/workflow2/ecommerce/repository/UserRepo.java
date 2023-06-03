package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * This repository help us to perform all database operation on user table
 * @author krishna_rawat
 * @version v0.0.1
 */
public interface UserRepo extends JpaRepository<User, UUID> {
    /**
     * This method help us to find user by email and password
     * @param email It takes String type of value which contains email of a user
     * @param Password It takes String type of value which contains password of a user
     * @return return user whose email and password provided as in the parameters
     */
    Optional<User> findOneByEmailAndPassword(String email, String Password);

    /**
     * This method return a user by matching its email
     * @param email  it takes email id of a user which is a type of String
     * @return return a user whose email is provided
     */
    User findByEmail(String email);
}
