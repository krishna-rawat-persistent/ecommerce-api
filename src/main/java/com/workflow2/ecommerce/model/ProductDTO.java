package com.workflow2.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String id;
    private String name;
    private String category;
    private String brand;
    private double price;
    private String color;
    private String size;
    private byte[] image = null;
}
