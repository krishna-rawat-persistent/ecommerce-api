package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
/**
 * This repository help us to perform all database operation on product table
 * @author krishna_rawat and nikhita_sripada
 * @version v0.0.1
 */
public interface ProductRepo extends JpaRepository<Product, String> {

    /**
     * This method find all the product whose name or description contains searchText
     * @param searchText It is a string type of value which contains searchText provided by user to find products
     * @return It returns list of products which contains the given text in its description or name of the products
     */
    @Query(value="select * from products p where p.product_name like %:search_text% or p.product_description like %:search_text%", nativeQuery=true)
    List<Product> findBySearchText(@Param("search_text") String searchText);

    /**
     * This method returns all the products which belongs to particular category give in parameter
     * @param category It is a String value which contains the name of category
     * @return List of all the products which belongs to the category given in the parameter
     */
    @Query(value="select * from products p where p.product_category = :category", nativeQuery=true)
    List<Product> findByCategory(@Param("category") String category);

    /**
     * This method deletes product by product ID
     * @param productId It's a Id of the product which we want to delete
     */
    void deleteById(UUID productId);

    /**
     * This Method help us to fetch product of given productId
     * @param productId This is the productId of product we want to fetch from database
     * @return Particular product whose id is given in the parameter
     */
    Product getReferenceById(UUID productId);
}
