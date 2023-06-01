package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.model.ProductDTO;
import com.workflow2.ecommerce.services.ProductService;
import com.workflow2.ecommerce.util.ImageUtility;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("api/product")
@CrossOrigin()
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductDTO> saveProduct(
            @RequestPart("product") Product product,
            @RequestPart("image") MultipartFile file) {
        try {
            Calendar calendar = Calendar.getInstance();
            String day = String.valueOf(calendar.get(Calendar.DATE));
            String month = String.valueOf(calendar.get(Calendar.MONTH));
            String hrs = String.valueOf(calendar.get(calendar.HOUR));
            String min = String.valueOf(calendar.get(Calendar.MINUTE));

            String prodId = product.getCategory().substring(0, 3) + product.getName().substring(0, 2) + day + month + hrs + min;

            Product product1 = Product.builder()
                    .id(prodId)
                    .name(product.getName())
                    .brand(product.getBrand())
                    .category(product.getCategory())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .totalStock(product.getTotalStock())
                    .image(ImageUtility.compressImage(file.getBytes()))
                    .size(product.getSize())
                    .color(product.getColor())
                    .subcategory(product.getSubcategory())
                    .build();

            return productService.saveProduct(product1);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('User','Admin')")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") String productId){
        return productService.getProduct(productId);
    }

    @GetMapping("/image/{id}")
    @PreAuthorize("hasAnyRole('User','Admin')")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") String productId){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(productService.getProduct(productId).getBody().getImage());
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('User','Admin')")
    public ResponseEntity<List<ProductDTO>> getAllProduct(){
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") String id, @RequestPart("product") Product product, @RequestPart("image") MultipartFile file){
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
                        .subcategory(product.getSubcategory())
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
                        .subcategory(product.getSubcategory())
                        .build();
                return productService.updateProduct(product1, id);
            }
        }catch (IOException ioe){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity deleteProductById(@PathVariable("id") String id){
            return productService.deleteProduct(id);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity deleteAllProduct(){
            return productService.deleteAllProducts();
    }
}
