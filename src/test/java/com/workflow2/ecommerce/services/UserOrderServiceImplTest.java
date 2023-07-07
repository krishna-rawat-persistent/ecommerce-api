package com.workflow2.ecommerce.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.*;
import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.repository.UserOrderCommonDao;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserOrderServiceImplTest {

    @InjectMocks
    UserOrderServiceImpl userOrderService;

    @Mock
    CartServiceImpl cartService;

    @Mock
    UserOrderCommonDao userOrderCommonDao;

    @Mock
    ProductServiceImpl productService;

    @Mock
    UserDao userDao;

    @Mock
    CartDao cartDao;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    CartDetails cartDetails;
    List<CartDetails> cartDetailsList;
    List<CartItems> cartItemsList;
    List<SimpleGrantedAuthority> roles;
    Cart cart;
    User user;
    CartItems cartItems;
    ProductDTO product;
    UserOrderCommon userOrderCommon;
    OrderDetails orderDetails;
    List<OrderDetails> orderDetailsList;
    List<UserOrderCommon> userOrderCommonList;

    @BeforeEach
    void setUp() {
        product = ProductDTO.builder().id(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).name("product1").category("furniture").ratings(4.5).brand("brand1").price(19999).totalStock(10).build();
        cartDetails = CartDetails.builder().id(122).productId(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).quantity(2).color("#17B383").size("M").build();
        cartItems = CartItems.builder().productId(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).quantity(2).image(null).name("Product1").shippingCharges(100).price(1299).color("#17B383").size("M").build();
        cartDetailsList = new ArrayList<>();
        cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItems);
        cartDetailsList.add(cartDetails);
        cart = Cart.builder().userCartId(1).totalAmount(9008.300000000001).cartDetails(cartDetailsList).build();
        orderDetails = OrderDetails.builder().trackingId(UUID.fromString("f06f324d-8698-4865-98e0-b8531ca36410")).quantity(1).status(1).productId(UUID.fromString("0e3a8c84-7020-499a-b904-1669582b7e14"))
                .shippingCharges(100.0).totalAmount(1000).date("2023-06-19").size("S").color("Olive").deliveryDate("2023-06-26").address("address").build();
        orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetails);
        userOrderCommon=UserOrderCommon.builder().orderId("123456789").totalAmount(1000).orderDetailsList(orderDetailsList).build();
        userOrderCommonList = new ArrayList<>();
        userOrderCommonList.add(userOrderCommon);
        user = User.builder().id(UUID.randomUUID()).name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").cart(cart).userOrders(userOrderCommonList).build();
        roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(userDao.findByEmail(any())).thenReturn(user);
        when(cartService.getUser(any())).thenReturn(user);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new org.springframework.security.core.userdetails.User("testmail@gmail.com", "Password123", roles));
    }

    @AfterEach
    void tearDown() {
        product=null;
        cartDetailsList = null;
        cartItemsList = null;
        cart = null;
        user = null;
        cartItems = null;
        reset(cartService);
    }

    @Test
    void placeOrder() {
//        when(cartService.getAllCartDetails(any())).thenReturn(cartItemsList);
//        when(productService.getProduct(any())).thenReturn(ResponseEntity.ok().body(product));
//        when(userOrderCommonDao.save(any())).thenReturn(UserOrderCommon.builder().orderId("123456789").build());
//        when(userDao.save(any())).thenReturn(user);
//        when(cartDao.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(cart));
//        when(cartDao.save(any())).thenReturn(cart);
//
//        String res = userOrderService.placeOrder(user, 1000, "address");
//
//        verify(cartService,times(1)).getAllCartDetails(any());
//        verify(productService,times(1)).getProduct(any());
//        verify(userOrderCommonDao,times(2)).save(any());
//        verify(userDao,times(1)).save(any());
//        verify(cartDao,times(1)).findById(anyInt());
//        verify(cartDao,times(1)).save(any());
//
//        assertThat(res).startsWith("Success");
    }

    @Test
    void viewAllOrders() {

    }

    @Test
    void trackOrder() {
    }
}