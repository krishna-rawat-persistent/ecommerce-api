package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * This repository help us to perform all database operation on Cart table
 * @author Tejas_Badjate
 * @version v0.0.1
 */
public interface CartRepo extends JpaRepository<Cart,Integer> {

}
