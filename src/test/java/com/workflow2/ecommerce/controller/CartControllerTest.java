package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.CartDetailDao;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.services.CartServiceImpl;
import com.workflow2.ecommerce.services.CustomUserDetailsService;
import com.workflow2.ecommerce.services.ProductServiceImpl;
import com.workflow2.ecommerce.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartServiceImpl cartService;

    @MockBean
    ProductServiceImpl productService;

    @MockBean
    UserDao userDao;

    @MockBean
    CartDao cartDao;

    @MockBean
    CartDetailDao cartDetailDao;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    CartDetails cartDetails1;
    List<CartDetails> cartDetails;
    List<SimpleGrantedAuthority> roles;
    Cart cart;
    User user;

    @BeforeEach
    void setUp() {
        cartDetails1  = CartDetails.builder().id(122).productId(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).quantity(2).color("#17B383").size("M").build();
        cartDetails = new ArrayList<>();
        cartDetails.add(cartDetails1);
        cart = Cart.builder().userCartId(1).totalAmount(9008.300000000001).cartDetails(cartDetails).build();
        user = User.builder().id(UUID.randomUUID()).name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").cart(cart).build();
        roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(userDao.findByEmail(any())).thenReturn(user);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new org.springframework.security.core.userdetails.User("testmail@gmail.com","Password123",roles));
    }

    @AfterEach
    void tearDown() {
        user=null;
        cart=null;
        cartDetails1=null;
        cartDetails=null;
        roles=null;
        reset(userDao);
    }

    @Test
    void addToCart() throws Exception {
        when(cartService.addToCart(any(),any())).thenReturn("Item Added to cart");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/addtocart")
                .content("{\"productId\":\"8b379426-eafa-4285-ad9c-45deb68a05a9\",\"quantity\":2,\"color\":\"#17B383\",\"size\":\"M\"}")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("Item Added to cart"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartService,times(1)).addToCart(any(),any());

    }

    @Test
    void getAllItems() throws Exception {
        when(cartDao.findById(any())).thenReturn(java.util.Optional.ofNullable(cart));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cart")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userCartId\":1,\"totalAmout\":9008.300000000001,\"cartDetails\":[{\"id\":122,\"productId\":\"8b379426-eafa-4285-ad9c-45deb68a05a9\",\"quantity\":2,\"color\":\"#17B383\",\"size\":\"M\"}]}"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartDao,times(1)).findById(any());
    }

    @Test
    void cartDetails() throws Exception {
        when(cartDao.findById(any())).thenReturn(java.util.Optional.ofNullable(cart));
        when(productService.getProduct(any())).thenReturn(ResponseEntity.ok().body(ProductDTO.builder().id(cartDetails1.getProductId()).name("Product1").color(new String[]{"#FFFFFF","#17B383"}).size("M").image(null).price(1299).build()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cartDetails")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"productId\":\"8b379426-eafa-4285-ad9c-45deb68a05a9\",\"quantity\":2,\"image\":null,\"name\":\"Product1\",\"price\":1299,\"color\":\"#17B383\",\"size\":\"M\"}]"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartDao,times(1)).findById(any());
        verify(productService, times(1)).getProduct(any());
    }

    @Test
    void cartDetailById() throws Exception {
        when(cartDao.findById(any())).thenReturn(java.util.Optional.ofNullable(cart));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cartDetails/8b379426-eafa-4285-ad9c-45deb68a05a9")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"productId\":\"8b379426-eafa-4285-ad9c-45deb68a05a9\",\"quantity\":2,\"color\":\"#17B383\",\"size\":\"M\"}"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartDao,times(1)).findById(any());
    }

    @Test
    void updateCartDetails() throws Exception {
        when(cartDao.findById(any())).thenReturn(java.util.Optional.ofNullable(cart));
        when(cartDao.save(any())).thenReturn(cart);
        when(cartDetailDao.save(any())).thenReturn(cartDetails1);
        when(productService.getProduct(any())).thenReturn(ResponseEntity.ok().body(ProductDTO.builder().id(cartDetails1.getProductId()).name("Product1").color(new String[]{"#FFFFFF","#17B383"}).size("M").image(null).price(1299).build()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/cartDetails")
                .content("{\"productId\":\"8b379426-eafa-4285-ad9c-45deb68a05a9\",\"quantity\":2,\"color\":\"#17B383\",\"size\":\"M\"}")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":122,\"productId\":\"8b379426-eafa-4285-ad9c-45deb68a05a9\",\"quantity\":2,\"color\":\"#17B383\",\"size\":\"M\"}"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartDao,times(1)).save(any());
        verify(cartDetailDao,times(1)).save(any());
        verify(productService,times(1)).getProduct(any());
    }

    @Test
    void cartDetailDeleteById() throws Exception {
        when(cartDao.findById(any())).thenReturn(java.util.Optional.ofNullable(cart));
        when(cartDao.save(any())).thenReturn(cart);
        doNothing().when(cartDetailDao).deleteById(any());
        when(productService.getProduct(any())).thenReturn(ResponseEntity.ok().body(ProductDTO.builder().id(cartDetails1.getProductId()).name("Product1").color(new String[]{"#FFFFFF","#17B383"}).size("M").image(null).price(1299).build()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/cartDetails/8b379426-eafa-4285-ad9c-45deb68a05a9")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartDao,times(1)).save(any());
        verify(cartDetailDao,times(1)).deleteById(any());
        verify(productService,times(1)).getProduct(any());
    }

    @Test
    void deleteAllCart() throws Exception {
        when(cartDao.findById(any())).thenReturn(java.util.Optional.ofNullable(cart));
        when(cartDao.save(any())).thenReturn(Cart.builder().build());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/cart")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("Cart is clear"))
                .andReturn();

        verify(userDao,times(1)).findByEmail(any());
        verify(cartDao,times(1)).save(any());
        verify(cartDao,times(1)).findById(any());
    }
}