package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.model.Login;
import com.workflow2.ecommerce.model.Register;
import com.workflow2.ecommerce.response.Response;
import com.workflow2.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/user")
public class LoginController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public Response register(@RequestBody Register register){
        return service.register(register);
    }

    @PostMapping("/login")
    public Response login(@RequestBody Login login){
        return service.login(login);
    }

}
