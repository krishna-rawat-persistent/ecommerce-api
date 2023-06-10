package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    User user;

    @Autowired
    UserDao userDao;

    @BeforeEach
    void setUp() {
        user = User.builder().id(UUID.randomUUID()).name("Test User").role("User").email("user@gmail.com").password("Password").phoneNo("0000000000").cart(new Cart()).build();
        userDao.save(user);
    }

    @AfterEach
    void tearDown() {
        userDao.deleteAll();

    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("All Test Start from Here");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("All Test ends here");
    }

    @Test
    @DisplayName("Should able to find user by email and password")
    void testFindOneByEmailAndPassword() {
        User user1 = userDao.findOneByEmailAndPassword(user.getEmail(),user.getPassword()).orElse(null);

        assertNotNull(user1);
    }

    @Test
    @DisplayName("Should able to find user by Email only")
    void testFindByEmail() {
        User user1 = userDao.findByEmail(user.getEmail());

        assertNotNull(user1,"User should be fetched");
    }
}