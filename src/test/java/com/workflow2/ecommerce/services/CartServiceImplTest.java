package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceImplTest {

    @Mock
    UserDao userDao;

    @Mock
    CartDao cartDao;

    @InjectMocks
    CartServiceImpl service;

    @Mock
    ProductServiceImpl productService;

    List<CartDetails> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add_to_cart() {
        CartDetails cartDetails1  = new CartDetails(1,UUID.randomUUID(),0,"#FFFFFF","L");
        List<CartDetails> cartDetails = new ArrayList<>();
        cartDetails.add(cartDetails1);
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Test Name")
                .email("testmail@gmail.com")
                .phoneNo("0987655432")
                .role("User")
                .password("Password123")
                .cart(Cart.builder().totalAmout(1000).cartDetails(cartDetails).build()).build();

        User user1 = User.builder()
                .id(UUID.randomUUID())
                .name("Test Name")
                .email("testmail@gmail.com")
                .phoneNo("0987655432")
                .role("User")
                .password("Password123")
                .cart(Cart.builder().totalAmout(1000).cartDetails(new ArrayList<>()).build()).build();


        when(userDao.findById(any())).thenReturn(java.util.Optional.ofNullable(user)).thenReturn(java.util.Optional.ofNullable(user1));

        ProductDTO product1 = ProductDTO.builder().id(UUID.randomUUID()).name("product1").category("furniture").ratings(4.5).brand("brand1").price(19999).totalStock(10).build();

        when(productService.getProduct(any())).thenReturn(ResponseEntity.ok().body(product1));

        Cart cart = Cart.builder().userCartId(1).cartDetails(cartDetails).totalAmout(1000).build();

        when(cartDao.save(any())).thenReturn(cart);

        service.add_to_cart(cartDetails1, user.getId());

        verify(userDao,times(1)).findById(any());

        service.add_to_cart(cartDetails1, user.getId());

        verify(userDao,times(2)).findById(any());

    }
}