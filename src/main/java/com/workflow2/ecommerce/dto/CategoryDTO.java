package com.workflow2.ecommerce.dto;

import com.workflow2.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private String name;
    private byte[] image;
    private Set<Product> products;

}