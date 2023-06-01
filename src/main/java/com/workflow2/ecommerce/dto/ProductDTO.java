package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private UUID id;
    private String name;
    private String category;
    private String brand;
    private double price;
    private String[] color;
    private String size;
    private byte[] image = null;
    private String description;
    private int totalStock;
    private String subcategory;
    private double ratings;

}
