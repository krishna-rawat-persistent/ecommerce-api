package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The Controller class have all the endpoint for searching a product
 * @author nikhitha_sripada
 * @version v0.0.1
 */
@RestController
@RequestMapping("api/search")
public class SearchController {
    @Autowired
    private ProductServiceImpl productService;

    /**
     * This method takes SearchText as String type and find all the product which contains that text
     * @param searchText It is a string value which have text we wanted to search
     * @return It returns list of products which contains the searchText in its name or in description
     */
    @GetMapping("/{search_text}")
    public ResponseEntity<List<ProductDTO>> searchProducts(@PathVariable("search_text") String searchText){
        return productService.getAllSearchedProduct(searchText);
    }
}