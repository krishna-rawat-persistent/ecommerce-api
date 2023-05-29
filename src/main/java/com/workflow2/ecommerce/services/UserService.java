package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.model.Login;
import com.workflow2.ecommerce.model.Register;
import com.workflow2.ecommerce.response.Response;

public interface UserService {
    Response login(Login login);
    Response register(Register register);
}
