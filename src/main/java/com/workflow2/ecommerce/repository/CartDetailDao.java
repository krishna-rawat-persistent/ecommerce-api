package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * This repository help us to perform all database operation on Cart_Details table
 * @author Tejas_Badjate
 * @version v0.0.1
 */
public interface CartDetailDao extends JpaRepository<CartDetails,Integer> {

    /**
     * This method remove details of particular product
     * @param productId It takes productId as input
     * @return List of Cart Details
     */
    public List<CartDetails> removeByProductId(String productId);
}
