package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, String> {
    @Query(value="select * from products p where p.product_name like %:search_text% or p.product_description like %:search_text%", nativeQuery=true)
    List<Product> findBySearchText(@Param("search_text") String searchText);

    @Query(value="select * from products p where p.product_category = :category", nativeQuery=true)
    List<Product> findByCategory(@Param("category") String category);

    void deleteById(UUID productId);

    Product getReferenceById(UUID productId);
}
