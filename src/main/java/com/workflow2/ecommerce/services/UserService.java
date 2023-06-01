package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.Login;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.dto.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Response> login(Login login);
    ResponseEntity<Response> register(Register register);

}
