package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.model.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ProductDTO> saveProduct(Product product);
    ResponseEntity<ProductDTO> getProduct(String productId);
    ResponseEntity<List<ProductDTO>> getAllProducts();
    ResponseEntity<ProductDTO> updateProduct(Product product,String productId);
    ResponseEntity deleteProduct(String productId);
    ResponseEntity deleteAllProducts();
}
