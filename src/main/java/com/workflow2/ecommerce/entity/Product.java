package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_category")
    private String category;

    @Column(name = "product_brand")
    private String brand;

    @Column(name = "product_price")
    private double price;

    @Column(name = "product_color")
    private String color;

    @Column(name = "product_size")
    private String size;

    @Column(name = "product_image", unique = false, nullable = true, length = 16777215)
    private byte[] image = null;

}