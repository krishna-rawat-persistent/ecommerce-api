package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * This is the Entity class for CartDetails table
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "shipping_charges")
    private double shippingCharges;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;
}
