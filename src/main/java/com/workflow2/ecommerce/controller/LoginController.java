package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.entity.AuthRequest;
import com.workflow2.ecommerce.model.Register;
import com.workflow2.ecommerce.response.Response;
import com.workflow2.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.workflow2.ecommerce.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


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

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody Register register){
        return service.register(register);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
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
