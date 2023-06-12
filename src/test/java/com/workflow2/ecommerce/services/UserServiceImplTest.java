package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.Login;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.dto.Response;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() {
        Login login = Login.builder().email("testmail@gmail.com").password("Password123").build();
        User user = User.builder().id(UUID.randomUUID()).name("Test Name").email("testmail@gmail.com").phoneNo("0000000000").password("Password123").role("User").cart(new Cart()).build();

        when(userDao.findByEmail(any())).thenReturn(user).thenReturn(null).thenReturn(user);
        when(userDao.findOneByEmailAndPassword(any(),any())).thenReturn(java.util.Optional.ofNullable(user)).thenReturn(java.util.Optional.empty());
        when(encoder.matches(any(),any())).thenReturn(true).thenReturn(false).thenReturn(true);

        ResponseEntity<Response> res = service.login(login);

        verify(userDao,times(1)).findByEmail(any());
        verify(userDao,times(1)).findOneByEmailAndPassword(any(),any());
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().getEmail()).isEqualTo(login.getEmail());

        ResponseEntity<Response> res1 = service.login(login);
        verify(userDao,times(2)).findByEmail(any());
        verify(userDao,times(1)).findOneByEmailAndPassword(any(),any());
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(res1.getBody().getStatus()).isFalse();

        ResponseEntity<Response> res2 = service.login(login);
        verify(userDao,times(3)).findByEmail(any());
        verify(userDao,times(1)).findOneByEmailAndPassword(any(),any());
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res2.getBody().getStatus()).isFalse();

        ResponseEntity<Response> res3 = service.login(login);
        verify(userDao,times(4)).findByEmail(any());
        verify(userDao,times(2)).findOneByEmailAndPassword(any(),any());
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res3.getBody().getStatus()).isFalse();
    }

    @Test
    void register() throws Exception {
        Register register = Register.builder().name("Test Name").email("testmail@gmail.com").password("Password123").phoneNo("0000000000").cart(new Cart()).role("User").build();
        User user = User.builder().id(UUID.randomUUID()).name("Test Name").email("testmail@gmail.com").phoneNo("0000000000").password("Password123").role("User").cart(new Cart()).build();

        when(userDao.findByEmail(register.getEmail())).thenReturn(null).thenReturn(null).thenReturn(null).thenReturn(user);
        when(userDao.save(any())).thenReturn(user).thenThrow(UsernameNotFoundException.class);

        ResponseEntity<Response> res = service.register(register);
        verify(userDao,times(1)).findByEmail(any());
        verify(userDao,times(1)).save(any());
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        register.setName("");
        ResponseEntity<Response> res1 = service.register(register);
        verify(userDao,times(2)).findByEmail(any());
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(res1.getBody().getStatus()).isFalse();
        assertThat(res1.getBody().getMessage()).isEqualTo("Please fill all the values");

        register.setName("Test Name");
        ResponseEntity<Response> res2 = service.register(register);
        verify(userDao,times(3)).findByEmail(any());
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(res2.getBody().getStatus()).isFalse();

        ResponseEntity<Response> res3 = service.register(register);
        verify(userDao,times(4)).findByEmail(any());
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(res3.getBody().getStatus()).isFalse();
        assertThat(res3.getBody().getEmail()).isEqualTo(register.getEmail());

    }
}