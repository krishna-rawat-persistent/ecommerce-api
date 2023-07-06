package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.repository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductRecommendationService {

    @Autowired
    private ProductDao productDao;

    public List<ProductDTO> recommendProduct(UUID productId){
        return null;
    }
}
