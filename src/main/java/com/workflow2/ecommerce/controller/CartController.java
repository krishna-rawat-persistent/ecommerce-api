package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;

import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.services.CartServiceImpl;
import com.workflow2.ecommerce.services.ProductServiceImpl;
import com.workflow2.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This controller class have all the methods required for cart functionality
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartServiceImpl service;

    @Autowired
    ProductServiceImpl prodService;

    @Autowired
    UserDao userDao;

    /**
     * This method help us to find user from the user from the request body
     * @param httpServletRequest It is the request body
     * @return It return the user we have find from the token
     */
    private User getUser(HttpServletRequest httpServletRequest){
        String data = httpServletRequest.getHeader("Authorization");
        String token = data.substring(7);
        String name = JwtUtil.extractUsername(token);
        User user = userDao.findByEmail(name);
        return user;
    }


    /**
     * This method adds items/products to the cart
     * @param cartDetails It has all the item and cart details
     * @param httpServletRequest It contains request body
     * @return It return string value which contains success message
     */
   @PostMapping("/addtocart")
    public String addToCart(@RequestBody CartDetails cartDetails, HttpServletRequest httpServletRequest){
        User user1 = service.getUser(httpServletRequest);
        return service.addToCart(cartDetails,user1);
    }

    /**
     * This endpoint return cart for the user who has login currently
     * @param httpServletRequest It contains request body
     * @return It returns cart for the user currently logged in
     */
    @GetMapping("/cart")
    public Optional<Cart> getAllCarts(HttpServletRequest httpServletRequest){
        User user = service.getUser(httpServletRequest);
        return service.getAllCart(user);
    }

    /**
     * This endpoint returns all the cart details for current user
     * @param httpServletRequest It contains request body
     * @return It returns CartDetails object which contains all the cart details for current user
     */
    @GetMapping("/cartDetails")
    public List<CartItems> getAllCartDetails(HttpServletRequest httpServletRequest){
        User user = service.getUser(httpServletRequest);
        return service.getAllCartDetails(user);
    }

    /**
     * This endpoint returns particular items details from cart by product id
     * @param httpServletRequest It contains request body
     * @param productId It is a String value which contains product as path variable
     * @return It returns cart detail object which have all the details related to product whose id is given
     */
    @GetMapping("/cartDetails/{productId}")
    public CartItems getCartDetailsById(HttpServletRequest httpServletRequest, @PathVariable UUID productId){
        User user = service.getUser(httpServletRequest);
        return service.getCartDetailsById(user,productId);
    }

    /**
     * This endpoint update the cartDetails within a cart
     * @param httpServletRequest It contains request body
     * @param cartDetails This is the cartDetails class object contains updated information of cart items
     * @return It returns cartDetails class object which contains updated item
     */
    @PutMapping("/cartDetails")
    public CartItems updateCartDetails(HttpServletRequest httpServletRequest, @RequestBody CartDetails cartDetails){
        User user = service.getUser(httpServletRequest);
        return service.updateCartDetails(user,cartDetails);
    }

    /**
     * This endpoint can delete cart details od an item from the product id
     * @param httpServletRequest It contains request body
     * @param productId It is a String value which contains product as path variable
     * @return It returns cart details after removing/deleting the item from the cart
     */
    @DeleteMapping("/cartDetails/{productId}")
    public List<CartItems> deleteCartById(HttpServletRequest httpServletRequest, @PathVariable UUID productId){
        User user = service.getUser(httpServletRequest);
        return service.deleteCartDetailsById(user,productId);
    }

    /**
     * This endpoint delete all the cartDetails from the cart
     * @param httpServletRequest It contains request body
     * @return It return String value with success message
     */
    @DeleteMapping("/cart")
    public String deleteAllCartDetails(HttpServletRequest httpServletRequest){
        User user = service.getUser(httpServletRequest);
        return service.deleteAllCartDetails(user);
    }
}
