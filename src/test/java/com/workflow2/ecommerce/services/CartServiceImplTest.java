package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.CartDetailDao;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceImplTest {

    @Mock
    UserDao userDao;

    @Mock
    CartDao cartDao;

    @Mock
    CartDetailDao cartDetailDao;

    @InjectMocks
    CartServiceImpl service;

    @Mock
    ProductServiceImpl productService;

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
        user = User.builder().id(UUID.randomUUID()).name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").cart(cart).build();
        roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_Admin"));
        when(userDao.findByEmail(any())).thenReturn(user);
        when(cartDao.findById(any())).thenReturn(Optional.ofNullable(cart));
        when(productService.getProduct(any())).thenReturn(ResponseEntity.ok().body(product));
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new org.springframework.security.core.userdetails.User("testmail@gmail.com","Password123",roles));
    }

    @AfterEach
    void tearDown() {
        user=null;
        cart=null;
        cartDetails =null;
        cartDetailsList =null;
        cartItemsList=null;
        roles=null;
        cartItems =null;
        reset(userDao);
        reset(cartDao);
        product =null;
    }

    @Test
    void add_to_cart() {
        when(cartDao.save(any())).thenReturn(cart);
        CartDetails cartDetails1 = CartDetails.builder().id(122).productId(UUID.fromString("8b379426-eafa-4285-ad9c-45deb68a05a9")).build();
        service.addToCart(cartDetails1, user);

        verify(cartDao,times(1)).save(any());
        verify(productService,times(1)).getProduct(any());

    }

    @Test
    void getAllCart() {
        Optional<Cart> res = service.getAllCart(user);
        verify(cartDao,times(1)).findById(any());
        assertThat(res.get().getUserCartId()).isEqualTo(1);
    }

    @Test
    void getAllCartDetails() {
        List<CartItems> res = service.getAllCartDetails(user);

        verify(cartDao,times(1)).findById(any());
        verify(productService,times(1)).getProduct(any());

        assertThat(res.size()).isGreaterThan(0);
    }

    @Test
    void getCartDetailsById() {
        CartItems res = service.getCartDetailsById(user, product.getId());

        verify(cartDao,times(1)).findById(any());
        verify(productService,times(1)).getProduct(any());

        assertThat(res).isNotNull();
        assertThat(res.getName()).isEqualTo("product1");
    }

    @Test
    void updateCartDetails() {
        when(cartDao.save(any())).thenReturn(cart);
        when(cartDetailDao.save(any())).thenReturn(cartDetails);

        CartItems res = service.updateCartDetails(user, cartDetails);

        verify(cartDao,times(1)).findById(any());
        verify(productService,times(1)).getProduct(any());
        verify(cartDao,times(1)).save(any());
        verify(cartDetailDao,times(1)).save(any());

        assertThat(res).isNotNull();
        assertThat(res.getName()).isEqualTo("product1");
    }

    @Test
    void deleteCartDetailsById() {
        when(cartDao.save(any())).thenReturn(cart);
        doNothing().when(cartDetailDao).deleteById(any());

        cartDetailsList.add(cartDetails);
        cartDetailsList.add(cartDetails);
        List<CartItems> res = service.deleteCartDetailsById(user, product.getId());

        verify(cartDao,times(1)).findById(any());
        verify(productService,times(3)).getProduct(any());
        verify(cartDao,times(1)).save(any());
        verify(cartDetailDao,times(1)).deleteById(any());

        assertThat(res).isNotNull();
        assertThat(res.size()).isEqualTo(2);
    }

    @Test
    void deleteAllCartDetails() {
        cart.setCartDetails(new ArrayList<>());
        when(cartDao.save(any())).thenReturn(cart);

        String res = service.deleteAllCartDetails(user);

        verify(cartDao,times(1)).findById(any());
        verify(cartDao,times(1)).save(any());

        assertThat(res).isEqualTo("Cart is clear");
    }

    @Test
    void getUser() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer "+ JwtUtil.generateToken("testmail@gmail.com"));
        User res = service.getUser(request);
        verify(userDao,times(1)).findByEmail(any());

        assertThat(res.getEmail()).isEqualTo("testmail@gmail.com");
    }
}