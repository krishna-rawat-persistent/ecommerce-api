package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.Login;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.dto.Response;
import org.springframework.http.ResponseEntity;

/**
 * This interface have login and signup module method structure
 * @author krishna_rawat
 * @version 0.0.1
 */
public interface UserService {
    ResponseEntity<Response> login(Login login);
    ResponseEntity<Response> register(Register register);

}
