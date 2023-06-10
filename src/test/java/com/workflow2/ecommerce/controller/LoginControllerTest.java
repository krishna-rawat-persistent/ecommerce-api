package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.AuthRequest;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.dto.Response;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.services.CustomUserDetailsService;
import com.workflow2.ecommerce.services.UserServiceImpl;
import com.workflow2.ecommerce.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDao userDao;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testRegister() throws Exception {
        Register register = Register.builder().name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").build();
        when(userService.register(register)).thenReturn(ResponseEntity.ok().body(Response.builder().email(register.getEmail()).message("User Registered Successfully !!").status(true).build()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/register")
                .content("{\"name\":\"Test Name\",\"email\":\"testmail@gmail.com\",\"phoneNo\":\"0000000000\",\"password\":\"Password123\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{message:\"User Registered Successfully !!\",status:true,email:testmail@gmail.com,jwtToken:null}"))
                .andReturn();


    }

    @Test
    void testGenerateToken() throws Exception {
        User user = User.builder().name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").build();
        AuthRequest request = AuthRequest.builder().userName("testmail@gmail.com").password("Password123").build();
        when(userDao.findByEmail(request.getUserName())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/authenticate")
                .content("{\"userName\":\"testmail@gmail.com\",\"password\":\"Password123\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"It is a Valid User!!\",\"status\":true,\"name\":\"Test Name\",\"email\":\"testmail@gmail.com\",\"phoneNo\":\"0000000000\",\"jwtToken\":"+JwtUtil.generateToken(request.getUserName())+"}"))
                .andReturn();
    }

    @Test
    void testGenerateTokenWhileInvalidUser() throws Exception {
        User user = User.builder().name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").build();
        AuthRequest request = AuthRequest.builder().userName("fake@gmail.com").password("Password123").build();
        when(userDao.findByEmail(request.getUserName())).thenThrow(new UsernameNotFoundException("No User Match !!"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/authenticate")
                .content("{\"userName\":\"fake@gmail.com\",\"password\":\"Password123\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"message\":\"Invalid userName/Password!!\",\"status\":false,\"name\":null,\"email\":\"fake@gmail.com\",\"phoneNo\":null,\"jwtToken\":null}"))
                .andReturn();
    }
}