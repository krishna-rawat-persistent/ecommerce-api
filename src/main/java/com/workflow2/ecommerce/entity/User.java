package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "user_name")
    private String name;

    @Column(name="user_email")
    private String email;

    @Column(name="user_phone_no")
    private String phoneNo;

    @Column(name="cart_id")
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="fk_cartId")
    private Cart cart;

    @Column(name="user_role")
    private String role;

    @Column(name = "password")
    private String password;
}