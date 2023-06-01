package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ResponseEntity<ProductDTO> saveProduct(Product product);
    ResponseEntity<ProductDTO> getProduct(UUID productId);
    ResponseEntity<List<ProductDTO>> getAllProducts();
    ResponseEntity<ProductDTO> updateProduct(Product product,UUID productId);
    ResponseEntity deleteProduct(UUID productId);
    ResponseEntity deleteAllProducts();
    ResponseEntity<List<ProductDTO>> getAllSearchedProduct(String searchText);
    ResponseEntity<List<ProductDTO>> getAllProductByCategory(String category);
}
