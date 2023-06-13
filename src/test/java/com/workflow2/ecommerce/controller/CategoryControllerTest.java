package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.services.CategoryServiceImpl;
import com.workflow2.ecommerce.services.CustomUserDetailsService;
import com.workflow2.ecommerce.util.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryServiceImpl service;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    CategoryDTO category1,category2,category3;


    @BeforeAll
    static void setUpForAll() throws Exception {
    }

    @AfterAll
    static void tearDownForAll(){

    }

    @BeforeEach
    void setUpForEach() {

        category1 = CategoryDTO.builder().name("Category1").build();
        category2 = CategoryDTO.builder().name("Category2").build();
        category3 = CategoryDTO.builder().name("Category3").build();
    }

    @AfterEach
    void tearDownForEach() {
        category1=category2=category3=null;
    }

    @Test
    void getAllCategory() throws Exception {

        when(service.getAllCategories()).thenReturn(Arrays.asList(category1,category2,category3));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/category/").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{},{}]"))
                .andExpect(content().json("[{name:Category1},{name:Category2},{name:Category3}]"))
                .andReturn();
    }

    @Test
    void getCategoryById() throws Exception {
        when(service.getCategoryById(category1.getName())).thenReturn(category1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/category/Category1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andExpect(content().json("{name:Category1}"))
                .andReturn();
    }

    @Test
    void save() throws Exception {

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new User("testmail@gmail.com","Password123",roles));

        when(service.saveCategory(any())).thenReturn(CategoryDTO.builder().name(category1.getName()).build()).thenThrow(Exception.class);

        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, (byte[]) null);

        MockMultipartFile categoryData =
                new MockMultipartFile("category",
                        "category",
                        String.valueOf(MediaType.APPLICATION_JSON),
                        "{\"name\":\"Category1\",\"image\":null}".getBytes()
                );

        MvcResult result = mockMvc.perform(multipart(HttpMethod.POST,"/api/category/")
                .file(categoryData)
                .file(file)
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                ).andExpect(status().isCreated())
                .andExpect(content().json("{}"))
                .andReturn();


        MvcResult result1 = mockMvc.perform(multipart(HttpMethod.POST,"/api/category/")
                .file(categoryData)
                .file(file)
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
        ).andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    void deleteById() throws Exception {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new User("testmail@gmail.com","Password123",roles));
        when(service.deleteCategoryById(any())).thenReturn("Successfully Deleted");

        MvcResult result = mockMvc.perform(delete("/api/category/category1")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
                ).andExpect(status().isOk())
                .andExpect(content().string("Successfully Deleted"))
                .andReturn();
    }

    @Test
    void deleteCategories() throws Exception {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new User("testmail@gmail.com","Password123",roles));
        when(service.deleteAllCategories()).thenReturn("Successfully Deleted All Categories");

        MvcResult result = mockMvc.perform(delete("/api/category/")
                .header("Authorization","Bearer "+JwtUtil.generateToken("testmail@gmail.com"))
        ).andExpect(status().isOk())
                .andExpect(content().string("Successfully Deleted All Categories"))
                .andReturn();
    }
}