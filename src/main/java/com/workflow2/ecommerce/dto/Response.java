package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the class which contains Response format for login and authentication
 * @author krishna_rawat
 * @version v0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    private String message;
    private Boolean status;
    private String email;
    private String jwtToken;


}
