package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/search")
@CrossOrigin("*")
public class SearchController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{search_text}")
    @PreAuthorize("hasAnyRole('User','Admin')")
    public ResponseEntity<List<ProductDTO>> searchProducts(@PathVariable("search_text") String searchText){
        return productService.getAllSearchedProduct(searchText);
    }
}