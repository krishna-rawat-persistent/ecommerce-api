package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * This is the Entity class for Product table
 * @author krishna_rawat & nikhitha_sripada
 * @version v0.0.1
 */
@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_category")
    private String category;

    @Column(name = "product_brand")
    private String brand;

    @Column(name = "product_price")
    private double price;

    @Column(name = "product_color")
    private String[] color;

    @Column(name = "product_size")
    private String size;

    @Column(name = "product_ratings")
    private double ratings;

    @Column(name = "product_image", unique = false, nullable = true, length = 16777215)
    private byte[] image = null;
    
    @Column(name="product_description")
    private String description;

    @Column(name="product_totalStock")
    private int totalStock;
}
