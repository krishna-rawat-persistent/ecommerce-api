package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Cart")
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userCartId;
    private HashMap<String,Integer> cartProduct = new HashMap<>();
    private Long totalAmout;



}
