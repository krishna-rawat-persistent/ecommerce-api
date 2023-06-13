package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.services.CustomUserDetailsService;
import com.workflow2.ecommerce.services.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductServiceImpl service;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void searchProducts() throws Exception {
        ProductDTO product1 = ProductDTO.builder().id(UUID.randomUUID()).name("product1").category("furniture").ratings(4.5).brand("brand1").price(19999).totalStock(10).build();
        ProductDTO product2 = ProductDTO.builder().id(UUID.randomUUID()).name("product2").category("lighting").ratings(4.1).brand("brand1").price(19999).totalStock(10).build();
        ProductDTO product3 = ProductDTO.builder().id(UUID.randomUUID()).name("product3").category("furniture").ratings(4.3).brand("brand1").price(19999).totalStock(10).build();
        when(service.getAllSearchedProduct(ArgumentMatchers.any())).thenReturn(ResponseEntity.ok().body(Arrays.asList(product1,product2,product3)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/search/someText").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{},{}]"))
                .andExpect(content().json("[{name:product1},{name:product2},{name:product3}]"))
                .andReturn();
    }
}