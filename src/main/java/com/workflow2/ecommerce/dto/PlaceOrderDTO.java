package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is the DTO class for placeOrder
 * @author tejas_badjate
 * @version v0.0.2
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaceOrderDTO {
    private double totalAmount;
    private String address;
}
