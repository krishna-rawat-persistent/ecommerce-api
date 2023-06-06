package com.workflow2.ecommerce.services.impl;

import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.dto.Login;
import com.workflow2.ecommerce.dto.Register;

import com.workflow2.ecommerce.dto.Response;
import com.workflow2.ecommerce.repository.UserDao;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is the implementation of user module which contains Login and Register method
 * @author krishna_rawat
 * @version v0.0.1
 */
@Service
public class UserServiceImpl {

    @Autowired
    private UserDao Dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method is validating the user and returning response as per the verification
     * @param login  - It takes the object of Login class which contains email and password as attribute
     * @return - it returns object of response entity class which have Response POJO as body
     */

    public ResponseEntity<Response> login(Login login) {
        User user = Dao.findByEmail(login.getEmail());
        if(user!=null){
            String plaintextPassword = login.getPassword();
            String encryptedPassword = user.getPassword();

            Boolean passwordMatched = passwordEncoder.matches(plaintextPassword,encryptedPassword);
            if(passwordMatched){
                Optional<User> validUser = Dao.findOneByEmailAndPassword(login.getEmail(),encryptedPassword);
                if(validUser.isPresent()){
                    return ResponseEntity.ok().body(Response.builder().status(true).message("Login Successful !!").email(login.getEmail()).build());
                }else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.builder().status(true).message("Invalid Credentials !!").build());
                }
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.builder().status(false).message("Credentials not Matching!!").build());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder().status(false).message("Email Not Exist !!").build());
    }

    /**
     * This method is validating the user inserting records to the database and also return conflict(409) status  if there is duplication in user data
     * @param register  - It takes the object of Register class which contains name, email, password, phoneNo, role and cart as attribute
     * @return - it returns object of response entity class which have Response POJO as body
     */

    public ResponseEntity<Response> register(Register register) {
        User user = Dao.findByEmail(register.getEmail());
        if(user==null) {
            if (register.getName().equals("") || register.getEmail().equals("") || register.getPassword().equals("")) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Response.builder().message("Please fill all the values").status(false).build());
            }
            try {
                Dao.save(User.builder().id(UUID.randomUUID())
                        .email(register.getEmail())
                        .name(register.getName())
                        .phoneNo(register.getPhoneNo())
                        .role("User")
                        .password(passwordEncoder.encode(register.getPassword()))
                        .cart(new Cart())
                        .build());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.builder().message("Some exception occurred").status(false).build());
            }
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.builder().email(register.getEmail()).message("Email Already Exist !!").status(false).build());
        }
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder().email(register.getEmail()).message("User Registered Successfully !!").status(true).build());

    }
}
