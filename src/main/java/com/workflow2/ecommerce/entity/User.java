package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the entity class for User
 * @author krishna_rawat
 * @version v0.0.1
 */
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

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="fk_cartId")
    private Cart cart;

    @Column(name="user_role")
    private String role;

    @Column(name = "password")
    private String password;

    @OneToMany(targetEntity = UserOrder.class,cascade = CascadeType.ALL)
    @JoinColumn(name="user_order_id")
    private List<UserOrder> userOrders = new ArrayList<>();
}