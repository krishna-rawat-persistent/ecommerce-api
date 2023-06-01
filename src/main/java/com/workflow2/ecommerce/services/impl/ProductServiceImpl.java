package com.workflow2.ecommerce.services.impl;


import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.repository.ProductRepo;
import com.workflow2.ecommerce.services.ProductService;
import com.workflow2.ecommerce.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo repository;

    @Override
    public ResponseEntity<ProductDTO> saveProduct(Product product) {
        try {
            product = repository.save(product);
            if(product==null){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand())
                    .category(product.getCategory())
                    .color(product.getColor())
                    .size(product.getSize())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .totalStock(product.getTotalStock())
                    .image(ImageUtility.decompressImage(product.getImage()))
                    .ratings(product.getRatings())
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<ProductDTO> getProduct(UUID productId) {
        try {
            final Optional<Product> prod = Optional.of(repository.getReferenceById(productId));
            if (prod==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ProductDTO.builder().id(prod.get().getId())
                    .name(prod.get().getName())
                    .category(prod.get().getCategory())
                    .brand(prod.get().getBrand())
                    .image(ImageUtility.decompressImage(prod.get().getImage()))
                    .price(prod.get().getPrice())
                    .size(prod.get().getSize())
                    .color(prod.get().getColor())
                    .description(prod.get().getDescription())
                    .totalStock(prod.get().getTotalStock())
                    .ratings(prod.get().getRatings())
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            List<Product> prods = repository.findAll();
            if (prods == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<ProductDTO> decompressProds = new ArrayList<>();
            for (Product p : prods) {
                p.setImage(ImageUtility.decompressImage(p.getImage()));
                ProductDTO prod = ProductDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .category(p.getCategory())
                        .brand(p.getBrand())
                        .image(p.getImage())
                        .size(p.getSize())
                        .color(p.getColor())
                        .price(p.getPrice())
                        .description(p.getDescription())
                        .totalStock(p.getTotalStock())
                        .ratings(p.getRatings())
                        .build();
                decompressProds.add(prod);
            }
            return ResponseEntity.status(HttpStatus.OK).body(decompressProds);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(Product product, UUID productId) {
        try {
            final Optional<Product> prod = Optional.of(repository.getReferenceById(productId));
            if (prod != null) {
                product.setId(prod.get().getId());
                repository.save(product);
                product.setImage(ImageUtility.decompressImage(prod.get().getImage()));
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .color(product.getColor())
                        .size(product.getSize())
                        .price(product.getPrice())
                        .image(product.getImage())
                        .description(product.getDescription())
                        .totalStock(product.getTotalStock())
                        .ratings(product.getRatings())
                        .build());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity deleteProduct(UUID productId) {
        try {
            repository.deleteById(productId);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete there is some error");
        }
    }

    @Override
    public ResponseEntity deleteAllProducts() {
        try {
            repository.deleteAll();
            return ResponseEntity.status(HttpStatus.OK).body("All products Deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete there is some error");
        }
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllSearchedProduct(String searchText) {
        try {
            List<Product> prods = repository.findBySearchText(searchText);
            if (prods == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<ProductDTO> decompressProds = new ArrayList<>();
            for (Product p : prods) {
                p.setImage(ImageUtility.decompressImage(p.getImage()));
                ProductDTO prod = ProductDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .category(p.getCategory())
                        .brand(p.getBrand())
                        .image(p.getImage())
                        .size(p.getSize())
                        .color(p.getColor())
                        .price(p.getPrice())
                        .description(p.getDescription())
                        .totalStock(p.getTotalStock())
                        .ratings(p.getRatings())
                        .build();
                decompressProds.add(prod);
            }
            return ResponseEntity.status(HttpStatus.OK).body(decompressProds);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProductByCategory(String category) {
        try {
            List<Product> prods = repository.findByCategory(category);
            if (prods == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<ProductDTO> decompressProds = new ArrayList<>();
            for (Product p : prods) {
                p.setImage(ImageUtility.decompressImage(p.getImage()));
                ProductDTO prod = ProductDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .category(p.getCategory())
                        .brand(p.getBrand())
                        .image(p.getImage())
                        .size(p.getSize())
                        .color(p.getColor())
                        .price(p.getPrice())
                        .description(p.getDescription())
                        .totalStock(p.getTotalStock())
                        .ratings(p.getRatings())
                        .build();
                decompressProds.add(prod);
            }
            return ResponseEntity.status(HttpStatus.OK).body(decompressProds);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}

