package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.CartDetailRepo;
import com.workflow2.ecommerce.repository.CartRepo;
import com.workflow2.ecommerce.repository.UserRepo;
import com.workflow2.ecommerce.services.impl.CartServiceImpl;
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
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepo userRepo;

    @Autowired
    CartRepo cartRepo;
    @Autowired
    CartDetailRepo cartDetailRepo;

    @Autowired
    CartServiceImpl service;

    /**
     * This method help us to find user from the user from the request body
     * @param httpServletRequest It is the request body
     * @return It return the user we have find from the token
     */
    private User getUser(HttpServletRequest httpServletRequest){
        String data = httpServletRequest.getHeader("Authorization");
        String token = data.substring(7);
        String name = jwtUtil.extractUsername(token);
        System.out.println(name);
        User user = userRepo.findByEmail(name);
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
        User user = getUser(httpServletRequest);
        UUID id = user.getId();
        return service.add_to_cart(cartDetails,id);
    }

    /**
     * This endpoint return cart for the user who has login currently
     * @param httpServletRequest It contains request body
     * @return It returns cart for the user currently logged in
     */
    @GetMapping("/cart")
    public Optional<Cart> getAllItems(HttpServletRequest httpServletRequest){
        User user = getUser(httpServletRequest);
        int id = user.getCart().getUserCartId();
        Cart cart = cartRepo.findById(id).get();
        return Optional.of(cart);
    }


    /**
     * This endpoint returns all the cart details for current user
     * @param httpServletRequest It contains request body
     * @return It returns CartDetails object which contains all the cart details for current user
     */
    @GetMapping("/cartDetails")
    public List<CartDetails> CartDetails(HttpServletRequest httpServletRequest){
        System.out.println("Inside cart details");
        User user = getUser(httpServletRequest);
        int id = user.getCart().getUserCartId();
        Cart cart = cartRepo.findById(id).get();
        System.out.println("cart: " + cart );
        List<CartDetails> list = cart.getCartDetails();
        return list;
    }

    /**
     * This endpoint returns particular items details from cart by product id
     * @param httpServletRequest It contains request body
     * @param productId It is a String value which contains product as path variable
     * @return It returns cart detail object which have all the details related to product whose id is given
     */
    @GetMapping("/cartDetails/{productId}")
    public CartDetails CartDetailById(HttpServletRequest httpServletRequest,@PathVariable String productId){
        User user = getUser(httpServletRequest);
        int id = user.getCart().getUserCartId();
        Cart cart = cartRepo.findById(id).get();
        List<CartDetails> list = cart.getCartDetails();
        CartDetails cartDetails;
        int x=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getProductId().equals(productId)){
                x=i;
                break;
            }
        }
        return list.get(x);
    }

    /**
     * This endpoint update the cartDetails within a cart
     * @param httpServletRequest It contains request body
     * @param cartDetails This is the cartDetails class object contains updated information of cart items
     * @return It returns cartDetails class object which contains updated item
     */
    @PutMapping("/cartDetails")
    public CartDetails updateCartDetails(HttpServletRequest httpServletRequest, @RequestBody CartDetails cartDetails){
        User user = getUser(httpServletRequest);
        int id = user.getCart().getUserCartId();
        Cart cart = cartRepo.findById(id).get();
        List<CartDetails> list = cart.getCartDetails();
        int  x=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getProductId().equals(cartDetails.getProductId())){
                x=i;
                break;
            }
        }
        CartDetails cartDetails1 = cart.getCartDetails().get(x);
        cart.setTotalAmout(cart.getTotalAmout() -(cartDetails1.getPrice() * cartDetails1.getQuantity()));
        cartDetails1.setProductId(cartDetails.getProductId());
        cartDetails1.setPrice(cartDetails.getPrice());
        cartDetails1.setQuantity(cartDetails.getQuantity());
        cart.setTotalAmout(cart.getTotalAmout() + (cartDetails1.getPrice() * cartDetails1.getQuantity()));
        cartRepo.save(cart);
        cartDetailRepo.save(cartDetails1);
        return cartDetails1;
    }

    /**
     * This endpoint can delete cart details od an item from the product id
     * @param httpServletRequest It contains request body
     * @param productId It is a String value which contains product as path variable
     * @return It returns cart details after removing/deleting the item from the cart
     */
    @DeleteMapping("/cartDetails/{productId}")
    public List<CartDetails> CartDetailDeleteById(HttpServletRequest httpServletRequest,@PathVariable String productId){
        User user = getUser(httpServletRequest);
        int id = user.getCart().getUserCartId();
        Cart cart = cartRepo.findById(id).get();
        List<CartDetails> list = cart.getCartDetails();
        int  x=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getProductId().equals(productId)){
                x=i;
                break;
            }
        }
        CartDetails cartDetails1 = cart.getCartDetails().get(x);
        int cartDetailsId = cartDetails1.getId();
        cart.setTotalAmout(cart.getTotalAmout() -(cartDetails1.getPrice() * cartDetails1.getQuantity()));
        cart.getCartDetails().remove(x);
        cart.setCartDetails(list);
        cartRepo.save(cart);
        cartDetailRepo.deleteById(cartDetailsId);
        return list;
    }

    /**
     * This endpoint delete all the cartDetails from the cart
     * @param httpServletRequest It contains request body
     * @return It return String value with success message
     */
    @DeleteMapping("/cart")
    public String deleteAllCart(HttpServletRequest httpServletRequest){
        User user = getUser(httpServletRequest);
        int id = user.getCart().getUserCartId();
        Cart cart = cartRepo.findById(id).get();
        cart.getCartDetails().clear();
        cart.setTotalAmout(0);
        cartRepo.save(cart);
        return "Cart is clear";
    }
}
