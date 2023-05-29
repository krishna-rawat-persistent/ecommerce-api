package com.workflow2.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Register {

    private String userName;
    private String email;
    private String phoneNo;
    private String role;
    private String password;
}