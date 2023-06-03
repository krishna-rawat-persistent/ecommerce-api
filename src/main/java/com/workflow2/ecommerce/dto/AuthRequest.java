package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class have required attributes and request body format for Authentication module
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String userName;
    private String password;
}
