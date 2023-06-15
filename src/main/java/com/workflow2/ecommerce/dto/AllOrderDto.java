package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

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
