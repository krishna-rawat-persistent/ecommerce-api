package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * This class is the Response type for cart object
 * @author krishna_rawat
 * @version 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {
    private UUID productId;
    private int quantity;
    private byte[] image;
    private String name;
    private double shippingCharges;
    private double price;
    private String color;
    private String size;
}
