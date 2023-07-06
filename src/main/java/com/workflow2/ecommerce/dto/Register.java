package com.workflow2.ecommerce.dto;

import com.workflow2.ecommerce.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class have the required attribute for registering an User
 * @author krishna_rawat
 * @version v0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Register {

    private String name;
    private String email;
    private String phoneNo;
    private String role;
    private String password;
    private String address;
    private Cart cart;
}