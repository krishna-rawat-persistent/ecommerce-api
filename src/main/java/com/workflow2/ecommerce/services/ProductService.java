package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.model.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO saveProduct(Product product);
    ProductDTO getProduct(String productId);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(Product product,String productId);
    void deleteProduct(String productId);
    void deleteAllProducts();
}
