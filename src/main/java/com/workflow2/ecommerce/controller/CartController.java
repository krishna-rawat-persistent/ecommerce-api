package com.workflow2.ecommerce.controller;

//import com.workflow2.ecommerce.dto.CartItems;
//import com.workflow2.ecommerce.dto.ProductDTO;
//import com.workflow2.ecommerce.entity.Cart;
//import com.workflow2.ecommerce.entity.CartDetails;
//import com.workflow2.ecommerce.entity.User;
//
//import com.workflow2.ecommerce.repository.CartDao;
//import com.workflow2.ecommerce.repository.CartDetailDao;
//import com.workflow2.ecommerce.repository.UserDao;
//import com.workflow2.ecommerce.services.CartServiceImpl;
//import com.workflow2.ecommerce.services.ProductServiceImpl;
//import com.workflow2.ecommerce.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
///**
// * This controller class have all the methods required for cart functionality
// * @author Tejas_Badjate
// * @version v0.0.1
// */
//@RestController
//@RequestMapping("/api")
//public class CartController {
//
//    @Autowired
//    UserDao userDao;
//
//    @Autowired
//    CartDao cartDao;
//
//    @Autowired
//    CartDetailDao cartDetailDao;
//
//    @Autowired
//    CartServiceImpl service;
//
//    @Autowired
//    ProductServiceImpl prodService;
//
//    /**
//     * This method help us to find user from the user from the request body
//     * @param httpServletRequest It is the request body
//     * @return It return the user we have find from the token
//     */
////    private User getUser(HttpServletRequest httpServletRequest){
////        String data = httpServletRequest.getHeader("Authorization");
////        String token = data.substring(7);
////        String name = JwtUtil.extractUsername(token);
////        System.out.println(name);
////        User user = userDao.findByEmail(name);
////        return user;
////    }
//
//
//    /**
//     * This method adds items/products to the cart
//     * @param cartDetails It has all the item and cart details
//     * @param httpServletRequest It contains request body
//     * @return It return string value which contains success message
//     */
//   @PostMapping("/addtocart")
//    public String addToCart(@RequestBody CartDetails cartDetails, HttpServletRequest httpServletRequest){
//        User user = service.getUser(httpServletRequest);
//        UUID id = user.getId();
//        return service.add_to_cart(cartDetails,id);
//    }
//
//    /**
//     * This endpoint return cart for the user who has login currently
//     * @param httpServletRequest It contains request body
//     * @return It returns cart for the user currently logged in
//     */
//    @GetMapping("/cart")
//    public Optional<Cart> getAllItems(HttpServletRequest httpServletRequest){
//        User user = service.getUser(httpServletRequest);
//        int id = user.getCart().getUserCartId();
//        Cart cart = cartDao.findById(id).get();
//        return Optional.of(cart);
//    }
//
//
//    /**
//     * This endpoint returns all the cart details for current user
//     * @param httpServletRequest It contains request body
//     * @return It returns CartDetails object which contains all the cart details for current user
//     */
//    @GetMapping("/cartDetails")
//    public List<CartItems> CartDetails(HttpServletRequest httpServletRequest){
//        User user = service.getUser(httpServletRequest);
//        int id = user.getCart().getUserCartId();
//        Cart cart = cartDao.findById(id).get();
//        List<CartDetails> list = cart.getCartDetails();
//        List<CartItems> cartItemsList = new ArrayList<>();
//        for(CartDetails c:list){
//            ProductDTO product = prodService.getProduct(c.getProductId()).getBody();
//            CartItems cartItems = CartItems.builder().productId(c.getProductId())
//                    .color(c.getColor())
//                    .size(c.getSize())
//                    .quantity(c.getQuantity())
//                    .image(product.getImage())
//                    .price(product.getPrice())
//                    .name(product.getName())
//                    .build();
//            cartItemsList.add(cartItems);
//        }
//
//        return cartItemsList;
//    }
//
//    /**
//     * This endpoint returns particular items details from cart by product id
//     * @param httpServletRequest It contains request body
//     * @param productId It is a String value which contains product as path variable
//     * @return It returns cart detail object which have all the details related to product whose id is given
//     */
//    @GetMapping("/cartDetails/{productId}")
//    public CartDetails CartDetailById(HttpServletRequest httpServletRequest,@PathVariable String productId){
//        User user = service.getUser(httpServletRequest);
//        int id = user.getCart().getUserCartId();
//        Cart cart = cartDao.findById(id).get();
//        List<CartDetails> list = cart.getCartDetails();
//
//        int x=0;
//        for(int i=0;i<list.size();i++){
//            if(list.get(i).getProductId().equals(productId)){
//                x=i;
//                break;
//            }
//        }
//        return list.get(x);
//    }
//
//    /**
//     * This endpoint update the cartDetails within a cart
//     * @param httpServletRequest It contains request body
//     * @param cartDetails This is the cartDetails class object contains updated information of cart items
//     * @return It returns cartDetails class object which contains updated item
//     */
//    @PutMapping("/cartDetails")
//    public CartDetails updateCartDetails(HttpServletRequest httpServletRequest, @RequestBody CartDetails cartDetails){
//        User user = getUser(httpServletRequest);
//        int id = user.getCart().getUserCartId();
//        Cart cart = cartDao.findById(id).get();
//        List<CartDetails> list = cart.getCartDetails();
//        int index =0;
//        for(int i=0;i<list.size();i++){
//            if(list.get(i).getProductId().equals(cartDetails.getProductId())){
//                index=i;
//                break;
//            }
//        }
//        CartDetails cartDetails1 = cart.getCartDetails().get(index);
//        ProductDTO product = prodService.getProduct(cartDetails1.getProductId()).getBody();
//        cart.setTotalAmout(cart.getTotalAmout() -(product.getPrice() * cartDetails1.getQuantity()));
//        cartDetails1.setProductId(cartDetails.getProductId());
//        cartDetails1.setQuantity(cartDetails.getQuantity());
//        cart.setTotalAmout(cart.getTotalAmout() + (product.getPrice() * cartDetails1.getQuantity()));
//        cartDao.save(cart);
//        cartDetailDao.save(cartDetails1);
//        return cartDetails1;
//    }
//
//    /**
//     * This endpoint can delete cart details od an item from the product id
//     * @param httpServletRequest It contains request body
//     * @param productId It is a String value which contains product as path variable
//     * @return It returns cart details after removing/deleting the item from the cart
//     */
//    @DeleteMapping("/cartDetails/{productId}")
//    public List<CartDetails> CartDetailDeleteById(HttpServletRequest httpServletRequest,@PathVariable String productId){
//        User user = getUser(httpServletRequest);
//        int id = user.getCart().getUserCartId();
//        Cart cart = cartDao.findById(id).get();
//        List<CartDetails> list = cart.getCartDetails();
//        int  x=0;
//        for(int i=0;i<list.size();i++){
//            if(list.get(i).getProductId().equals(productId)){
//                x=i;
//                break;
//            }
//        }
//        CartDetails cartDetails1 = cart.getCartDetails().get(x);
//        ProductDTO product = prodService.getProduct(cartDetails1.getProductId()).getBody();
//        int cartDetailsId = cartDetails1.getId();
//        cart.setTotalAmout(cart.getTotalAmout() -(product.getPrice() * cartDetails1.getQuantity()));
//        cart.getCartDetails().remove(x);
//        cart.setCartDetails(list);
//        cartDao.save(cart);
//        cartDetailDao.deleteById(cartDetailsId);
//        return list;
//    }
//
//    /**
//     * This endpoint delete all the cartDetails from the cart
//     * @param httpServletRequest It contains request body
//     * @return It return String value with success message
//     */
//    @DeleteMapping("/cart")
//    public String deleteAllCart(HttpServletRequest httpServletRequest){
//        User user = getUser(httpServletRequest);
//        int id = user.getCart().getUserCartId();
//        Cart cart = cartDao.findById(id).get();
//        cart.getCartDetails().clear();
//        cart.setTotalAmout(0);
//        cartDao.save(cart);
//        return "Cart is clear";
//    }
//}


import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;

import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.services.CartServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartServiceImpl service;

    @PostMapping("/addtocart")
    public String addToCart(@RequestBody CartDetails cartDetails, HttpServletRequest httpServletRequest){
        User user1 = service.getUser(httpServletRequest);
        return service.add_to_cart(cartDetails,user1);
    }

    @GetMapping("/cart")
    public Optional<Cart> getallcart(HttpServletRequest httpServletRequest){
        User user = service.getUser(httpServletRequest);
        return service.getallcart(user);
    }

    @GetMapping("/cartDetails")
    public List<CartDetails> CartDeatils(HttpServletRequest httpServletRequest){
        User user = service.getUser(httpServletRequest);
        return service.CartDeatils(user);
    }

    @GetMapping("/cartDetails/{productId}")
    public CartDetails CartDeatilById(HttpServletRequest httpServletRequest,@PathVariable UUID productId){
        User user = service.getUser(httpServletRequest);
        return service.CartDeatilById(user,productId);
    }

    @PutMapping("/cartDetails")
    public CartDetails updateCartDetails(HttpServletRequest httpServletRequest, @RequestBody CartDetails cartDetails){
        User user = service.getUser(httpServletRequest);
        return service.updateCartDetails(user,cartDetails);
    }

    @DeleteMapping("/cartDetails/{productId}")
    public List<CartDetails> CartDeatilDeleteById(HttpServletRequest httpServletRequest,@PathVariable UUID productId){
        User user = service.getUser(httpServletRequest);
        return service.CartDeatilDeleteById(user,productId);
    }

    @DeleteMapping("/cart")
    public String deleteAllCart(HttpServletRequest httpServletRequest){
        User user = service.getUser(httpServletRequest);
        return service.deleteAllCart(user);
    }
}
