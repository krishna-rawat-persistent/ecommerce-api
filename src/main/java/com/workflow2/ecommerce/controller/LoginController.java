package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.AuthRequest;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.dto.Response;
import com.workflow2.ecommerce.services.UserService;
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
@CrossOrigin(origins = "*")
@RequestMapping("api/user")
public class LoginController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;
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
     * @param authRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Response> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.OK).body(Response.builder().message("Invalid userName/Password!!").status(false).email(authRequest.getUserName()).build());
        }
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder().message("It is a Valid User!!").status(true).email(authRequest.getUserName()).jwtToken(jwtUtil.generateToken(authRequest.getUserName())).build());
    }
}
