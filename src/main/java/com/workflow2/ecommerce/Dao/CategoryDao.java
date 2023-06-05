package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * This repository help us to perform all database operation on category table
 * @author Mayur_Jadhav
 * @version v0.0.1
 */
public interface CategoryRepo extends JpaRepository<Category,String>{
}
