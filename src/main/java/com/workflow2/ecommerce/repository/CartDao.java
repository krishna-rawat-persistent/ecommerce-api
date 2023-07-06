package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.annotation.Native;
import java.util.UUID;

/**
 * This repository help us to perform all database operation on Cart table
 * @author Tejas_Badjate
 * @version v0.0.1
 */
public interface CartDao extends JpaRepository<Cart,Integer> {


}
