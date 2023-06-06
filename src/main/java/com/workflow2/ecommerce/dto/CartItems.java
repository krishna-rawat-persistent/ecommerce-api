package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {
    private UUID productId;
    private int quantity;
    private byte[] image;
    private String name;
    private double price;
    private String color;
    private String size;
}
