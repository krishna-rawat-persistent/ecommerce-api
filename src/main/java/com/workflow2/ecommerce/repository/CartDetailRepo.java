package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDetailRepo extends JpaRepository<CartDetails,Integer> {
    public List<CartDetails> removeByProductId(String productId);
}
