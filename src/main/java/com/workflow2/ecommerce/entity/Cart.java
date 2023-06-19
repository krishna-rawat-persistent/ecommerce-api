package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Entity class for Cart table
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Cart")
@Entity
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cart_id")
    private Integer userCartId;

    @Column(name = "total_amount")
    private double totalAmount;

    @OneToMany(targetEntity = CartDetails.class,cascade = CascadeType.ALL)
    @JoinColumn(name="cd_fk")
    private List<CartDetails> cartDetails = new ArrayList<>();
}
