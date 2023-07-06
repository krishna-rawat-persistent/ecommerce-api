package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is the response object for Login
 * @author krishna_rawat
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private Boolean status;
    private String name;
    private String email;
    private String phoneNo;
    private String address;
    private String jwtToken;
}