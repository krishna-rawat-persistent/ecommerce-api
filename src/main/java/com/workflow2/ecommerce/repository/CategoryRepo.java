package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,String>{
}
