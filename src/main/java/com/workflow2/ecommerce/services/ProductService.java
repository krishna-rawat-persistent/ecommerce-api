package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * This interface have method structure for all product specific operations
 * @author krishna_rawat
 * @version 0.0.1
 */
public interface ProductService {
    ResponseEntity<ProductDTO> saveProduct(Product product);
    ResponseEntity<ProductDTO> getProduct(UUID productId);
    ResponseEntity<List<Product>> getAllProducts();
    ResponseEntity<ProductDTO> updateProduct(Product product,UUID productId);
    ResponseEntity deleteProduct(UUID productId);
    ResponseEntity deleteAllProducts();
    ResponseEntity<List<ProductDTO>> getAllSearchedProduct(String searchText);
    ResponseEntity<List<ProductDTO>> getAllProductByCategory(String category);
}
