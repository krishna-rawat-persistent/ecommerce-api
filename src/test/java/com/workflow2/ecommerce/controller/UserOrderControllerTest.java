package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.AllOrderDto;
import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.services.CartServiceImpl;
import com.workflow2.ecommerce.services.CustomUserDetailsService;
import com.workflow2.ecommerce.services.UserOrderServiceImpl;
import com.workflow2.ecommerce.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserOrderController.class)
class UserOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartServiceImpl cartService;

    @MockBean
    UserOrderServiceImpl userOrderService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    UserDao userDao;

    CartDetails cartDetails;
    List<CartDetails> cartDetailsList;
    List<CartItems> cartItemsList;
    List<SimpleGrantedAuthority> roles;
    Cart cart;
    User user;
    CartItems cartItems;

    @BeforeEach
    void setUp() {
        cartDetails = CartDetails.builder().id(122).productId(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).quantity(2).color("#17B383").size("M").build();
        cartItems = CartItems.builder().productId(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).quantity(2).image(null).name("Product1").shippingCharges(100).price(1299).color("#17B383").size("M").build();
        cartDetailsList = new ArrayList<>();
        cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItems);
        cartDetailsList.add(cartDetails);
        cart = Cart.builder().userCartId(1).totalAmount(9008.300000000001).cartDetails(cartDetailsList).build();
        user = User.builder().id(UUID.randomUUID()).name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").cart(cart).build();
        roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(userDao.findByEmail(any())).thenReturn(user);
        when(cartService.getUser(any())).thenReturn(user);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new org.springframework.security.core.userdetails.User("testmail@gmail.com", "Password123", roles));
    }

    @AfterEach
    void tearDown() {
        cartDetailsList = null;
        cartItemsList = null;
        cart = null;
        user = null;
        cartItems = null;
        reset(cartService);
    }

    @Test
    void placeOrder() throws Exception {
        when(userOrderService.placeOrder(any(), anyDouble(), anyString())).thenReturn("Success order_Bj045362").thenReturn("There is no item in cart!!");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/placeorder/1000/address")
                .header("Authorization", "Bearer " + JwtUtil.generateToken("testmail@gmail.com"));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("order_Bj045362"))
                .andReturn();

        verify(cartService, times(1)).getUser(any());
        verify(userOrderService, times(1)).placeOrder(any(), anyDouble(), anyString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("There is no item in cart!!"))
                .andReturn();

        verify(cartService, times(2)).getUser(any());
        verify(userOrderService, times(2)).placeOrder(any(), anyDouble(), anyString());
    }

    @Test
    void viewOrders() throws Exception {
        List<AllOrderDto> allOrderList = new ArrayList<>();
        AllOrderDto allOrders = AllOrderDto.builder().orderId("153efd9c-7577-466d-bbf6-0c15f9047319").status("Order Placed").orderedDate("2023-06-19").deliveryDate("2023-06-26")
                .color("Olive").image(null).description("some Description").productName("product1").trackingId(UUID.fromString("f06f324d-8698-4865-98e0-b8531ca36410")).size("M").build();
        allOrderList.add(allOrders);
        when(userOrderService.viewAllOrders(any())).thenReturn(allOrderList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/vieworders")
                .header("Authorization", "Bearer " + JwtUtil.generateToken("testmail@gmail.com"));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"orderId\":\"153efd9c-7577-466d-bbf6-0c15f9047319\",\"status\":\"Order Placed\",\"deliveryDate\":\"2023-06-26\",\"orderedDate\":\"2023-06-19\",\"image\":null,\"productName\":\"product1\",\"description\":\"some Description\",\"size\":\"M\",\"color\":\"Olive\",\"trackingId\":\"f06f324d-8698-4865-98e0-b8531ca36410\"}]"))
                .andReturn();

        verify(cartService, times(1)).getUser(any());
        verify(userOrderService, times(1)).viewAllOrders(any());
    }

    @Test
    void trackOrder() throws Exception {
        OrderDto order = OrderDto.builder().orderId("153efd9c-7577-466d-bbf6-0c15f9047319").userName("Test User").email("testemail@gmail.com").contactNo("9876543210")
                .quantity(1).deliveryAddress("17, brilliant center, Indore, MP, 452003").status("Order Placed").productId(UUID.fromString("0e3a8c84-7020-499a-b904-1669582b7e14"))
                .productName("product1").productImage(null).discountedPrice(19124.24).shippingCharges(100.0).size("S").color("Olive").orderedDate("2023-06-19").deliveryDate("2023-06-26")
                .orderTotal(19224.24).description("some description").build();
        when(userOrderService.trackOrder(any(), any(), any())).thenReturn(order);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/trackorder/153efd9c-7577-466d-bbf6-0c15f9047319/f06f324d-8698-4865-98e0-b8531ca36410")
                .header("Authorization", "Bearer " + JwtUtil.generateToken("testmail@gmail.com"));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderId\":\"153efd9c-7577-466d-bbf6-0c15f9047319\",\"userName\":\"Test User\",\"email\":\"testemail@gmail.com\"," +
                        "\"contactNo\":\"9876543210\",\"quantity\":1,\"deliveryAddress\":\"17, brilliant center, Indore, MP, 452003\",\"status\":\"Order Placed\"," +
                        "\"productId\":\"0e3a8c84-7020-499a-b904-1669582b7e14\",\"productName\":\"product1\",\"productImage\":null,\"discountedPrice\":19124.24," +
                        "\"shippingCharges\":100.0,\"size\":\"S\",\"color\":\"Olive\",\"deliveryDate\":\"2023-06-26\",\"orderedDate\":\"2023-06-19\",\"orderTotal\":19224.24," +
                        "\"description\":\"some description\"}"))
                .andReturn();

        verify(cartService, times(1)).getUser(any());
        verify(userOrderService, times(1)).trackOrder(any(),any(),any());
    }
}