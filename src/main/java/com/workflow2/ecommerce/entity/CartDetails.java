package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This is the Entity class for CartDetails table
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String productId;
    private int quantity;
    private double price;
}
