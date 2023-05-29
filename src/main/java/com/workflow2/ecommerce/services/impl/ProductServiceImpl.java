package com.workflow2.ecommerce.services.impl;


import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.model.ProductDTO;
import com.workflow2.ecommerce.repository.ProductRepo;
import com.workflow2.ecommerce.services.ProductService;
import com.workflow2.ecommerce.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo repository;

    @Override
    public ProductDTO saveProduct(Product product) {
        product = repository.save(product);
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .color(product.getColor())
                .size(product.getSize())
                .price(product.getPrice())
                .image(ImageUtility.decompressImage(product.getImage()))
                .build();
    }

    @Override
    public ProductDTO getProduct(String productId) {
        final Optional<Product> prod = Optional.of(repository.getReferenceById(productId));
        return ProductDTO.builder().id(prod.get().getId())
                .name(prod.get().getName())
                .category(prod.get().getCategory())
                .brand(prod.get().getBrand())
                .image(ImageUtility.decompressImage(prod.get().getImage()))
                .price(prod.get().getPrice())
                .size(prod.get().getSize())
                .color(prod.get().getColor()).build();
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> prods =  repository.findAll();
        List<ProductDTO> decompressProds = new ArrayList<>();
        for(Product p: prods){
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
                    .build();
            decompressProds.add(prod);
        }
       return decompressProds;
    }

    @Override
    public ProductDTO updateProduct(Product product, String productId) {
        final Optional<Product> prod = Optional.of(repository.getReferenceById(productId));
        if(prod!=null){
            product.setId(prod.get().getId());
            repository.save(product);
            product.setImage(ImageUtility.decompressImage(prod.get().getImage()));
            return ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand())
                    .category(product.getCategory())
                    .color(product.getColor())
                    .size(product.getSize())
                    .price(product.getPrice())
                    .image(product.getImage())
                    .build();
        }
        return null;
    }

    @Override
    public void deleteProduct(String productId) {
        repository.deleteById(productId);

    }

    @Override
    public void deleteAllProducts() {
        repository.deleteAll();
    }

}

