package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class have required attributes and request body format for login and authentication module
 * @author krishna_rawat
 * @version v0.0.1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Login {

    private String email;
    private String password;
}