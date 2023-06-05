package com.workflow2.ecommerce.dto;

import com.workflow2.ecommerce.entity.Category;
import com.workflow2.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * This class is used to return the response for Category
 * @author Mayur_Jadhav
 * @version v0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private String name;
    private byte[] image;
    private Set<Product> products;
    

}
