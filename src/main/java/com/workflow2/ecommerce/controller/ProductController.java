package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.services.ProductService;
import com.workflow2.ecommerce.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductDTO> saveProduct(
            @RequestPart("product") Product product,
            @RequestPart("image") MultipartFile file) {
        try {
            Product product1 = Product.builder()
                    .id(UUID.randomUUID())
                    .name(product.getName())
                    .brand(product.getBrand())
                    .category(product.getCategory())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .totalStock(product.getTotalStock())
                    .image(ImageUtility.compressImage(file.getBytes()))
                    .size(product.getSize())
                    .color(product.getColor())
                    .ratings(product.getRatings())
                    .build();

            return productService.saveProduct(product1);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") UUID productId){
        return productService.getProduct(productId);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") UUID productId){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(productService.getProduct(productId).getBody().getImage());
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getAllProduct(){
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") UUID id, @RequestPart("product") Product product, @RequestPart("image") MultipartFile file){
        ProductDTO prod = null;
        try{
            if(product.getImage()==null) {
                Product product1 = Product.builder()
                        .name(product.getName())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .image(ImageUtility.compressImage(file.getBytes()))
                        .color(product.getColor())
                        .size(product.getSize())
                        .description(product.getDescription())
                        .totalStock(product.getTotalStock())
                        .ratings(product.getRatings())
                        .build();
                return productService.updateProduct(product1, id);
            } else {
                Product product1 = Product.builder()
                        .name(product.getName())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .image(ImageUtility.compressImage(product.getImage()))
                        .color(product.getColor())
                        .size(product.getSize())
                        .description(product.getDescription())
                        .totalStock(product.getTotalStock())
                        .ratings(product.getRatings())
                        .build();
                return productService.updateProduct(product1, id);
            }
        }catch (IOException ioe){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity deleteProductById(@PathVariable("id") UUID id){
            return productService.deleteProduct(id);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity deleteAllProduct(){
            return productService.deleteAllProducts();
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('User','Admin')")
    public ResponseEntity<List<ProductDTO>> getAllProductByCategory(@PathVariable("category") String category){
        return productService.getAllProductByCategory(category);
    }


}
