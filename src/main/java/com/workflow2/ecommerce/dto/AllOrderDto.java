package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * This is the DTO class for all orders
 * @author tejas_badjate
 * @version v0.0.2
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllOrderDto {
    UUID orderId;
    String status;
    String deliveryDate;
    String orderedDate;
    byte[] image;
    String productName;
    String description;
    String size;
    String color;
    UUID trackingId;


}
