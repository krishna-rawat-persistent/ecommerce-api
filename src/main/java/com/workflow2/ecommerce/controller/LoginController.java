package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.AuthRequest;
import com.workflow2.ecommerce.dto.LoginResponse;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.dto.Response;

import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.workflow2.ecommerce.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *This class help us in registering and authenticating user
 * It contains contains register and authenticate endpoint with api/user mapping
 *
 * @author krishna_rawat & Tejas Badjate
 * @version v0.0.1
 */
@RestController
@RequestMapping("api/user")
public class LoginController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private UserDao repo;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * This method is used to register a user
     *
     * @param register This parameter is a object which have name, email, phone_no, role and password as attribute
     * @return it return's Object of Response entity class which have Response object inside body
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody Register register){
        return service.register(register);
    }


    /**
     * This method authenticate the user also identify it's role and then return JWT token
     * @param authRequest it take Authentication Request(AuthRequest) Parameter which have userName and password as attributes
     * @return -it return's Object of Response entity class which have Response object inside body
     */
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> generateToken(@RequestBody AuthRequest authRequest) {
        User user =null;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
            user = repo.findByEmail(authRequest.getUserName());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(LoginResponse.builder().message("Invalid userName/Password!!").status(false).email(authRequest.getUserName()).build());
        }
        return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.builder().message("It is a Valid User!!").status(true).email(authRequest.getUserName()).name(user.getName()).phoneNo(user.getPhoneNo()).jwtToken(JwtUtil.generateToken(authRequest.getUserName())).build());

    }
}
