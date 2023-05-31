package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.model.Login;
import com.workflow2.ecommerce.model.Register;
import com.workflow2.ecommerce.response.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Response> login(Login login);
    ResponseEntity<Response> register(Register register);
}
