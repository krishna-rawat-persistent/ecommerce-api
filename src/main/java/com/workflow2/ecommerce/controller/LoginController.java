package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.entity.AuthRequest;
import com.workflow2.ecommerce.model.Login;
import com.workflow2.ecommerce.model.Register;
import com.workflow2.ecommerce.response.Response;
import com.workflow2.ecommerce.services.UserService;
import com.workflow2.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")

public class LoginController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String home(){
        return "home Page";
    }

    @PostMapping("/register")
    public Response register(@RequestBody Register register){
        return service.register(register);
    }

//    @PostMapping("/login")
//    public Response login(@RequestBody Login login){
//        return service.login(login);
//    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        System.out.println(authRequest.getUserName());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }

    @GetMapping("/home")
    public String data(HttpServletRequest httpServletRequest){
        if (jwtUtil.check_role(httpServletRequest.getHeader("Authorization")))
            return "Home page and has admin access";
        else
            return "Home page and has user access";
    }

}
