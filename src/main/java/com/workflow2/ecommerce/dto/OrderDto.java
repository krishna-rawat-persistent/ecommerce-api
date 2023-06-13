package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDto {
    UUID orderId;
    String name;
    String email;
    String contactNo;
    double orderTotal;
    int quantity;
    String deliveryAddress;
    String status;
    LocalDate deliveryDate;
    UUID productId;

}
