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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class contains implementations of all the methods to perform operations on cart functionality
 *
 * @author Tejas_Badjate
 * @version v0.0.1
 */

@Service
public class CartServiceImpl {


    @Autowired
    UserDao userDao;

    @Autowired
    CartDao cartDao;

    @Autowired
    CartDetailDao cartDetailDao;

    @Autowired
    ProductServiceImpl productService;

    public User getUser(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String name = JwtUtil.extractUsername(token);
        return userDao.findByEmail(name);
    }

    public int findCartDetailsIndex(UUID productId, List<CartDetails> list) {
        int productIndex = 0;
        for (int currentIndex = 0; currentIndex < list.size(); currentIndex++) {
            if (list.get(currentIndex).getProductId().equals(productId)) {
                productIndex = currentIndex;
                break;
            }
        }
        return productIndex;
    }

    public CartItems convertToCartItems(CartDetails cartDetails, ProductDTO product){
        return CartItems.builder()
                .productId(cartDetails.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .image(product.getImage())
                .quantity(cartDetails.getQuantity())
                .size(cartDetails.getSize())
                .shippingCharges(cartDetails.getShippingCharges())
                .color(cartDetails.getColor()).build();
    }

    public String addToCart(CartDetails cartDetails, User user) {
        Cart cart = user.getCart();
        ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();

        double totalAmount = cart.getTotalAmount();
        totalAmount = totalAmount + (cartDetails.getQuantity() * product.getPrice());

        cart.setTotalAmount(totalAmount);
        if(cartDetails.getQuantity()==0) {
            cartDetails.setQuantity(1);
        }
        if(cartDetails.getColor() == null){
            cartDetails.setColor("#000000");
        }
        if(cartDetails.getSize() == null){
            cartDetails.setColor("M");
        }
        if(cartDetails.getShippingCharges() == 0){
            cartDetails.setShippingCharges(100);
        }

        List<CartDetails> cartDetailsList = cart.getCartDetails();
        cartDetailsList.add(cartDetails);
        cart.setCartDetails(cartDetailsList);

        cartDao.save(cart);
        return "Item Added to cart!!";
    }

    public Optional<Cart> getAllCart(User user) {
        int userId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(userId).get();
        return Optional.of(cart);
    }

    public List<CartItems> getAllCartDetails(User user) {
        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).get();
        List<CartItems> cartItemsList = new ArrayList<>();
        for (CartDetails cartDetails : cart.getCartDetails()) {
            ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();
            cartItemsList.add(convertToCartItems(cartDetails,product));
        }
        return cartItemsList;
    }

    public CartItems getCartDetailsById(User user, UUID productId) {
        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).get();
        List<CartDetails> cartDetailsList = cart.getCartDetails();
        int cartDetailsIndex = findCartDetailsIndex(productId, cartDetailsList);
        CartDetails cartDetails = cartDetailsList.get(cartDetailsIndex);
        ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();
        return convertToCartItems(cartDetails,product);
    }

    public CartItems updateCartDetails(User user, CartDetails cartDetails) {

        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        UUID productId = cartDetails.getProductId();
        ProductDTO product = productService.getProduct(productId).getBody();
        List<CartDetails> cartDetailsList = cart.getCartDetails();
        int cartDetailsIndex = findCartDetailsIndex(productId, cartDetailsList);

        CartDetails cartDetails1 = cart.getCartDetails().get(cartDetailsIndex);
        cart.setTotalAmount(cart.getTotalAmount() - (product.getPrice() * cartDetails1.getQuantity()));
        cartDetails1.setProductId(cartDetails.getProductId());
        cartDetails1.setQuantity(cartDetails.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount() + (product.getPrice() * cartDetails1.getQuantity()));

        cartDao.save(cart);
        cartDetailDao.save(cartDetails1);
        return convertToCartItems(cartDetails1,product);
    }

    public List<CartItems> deleteCartDetailsById(User user, UUID productId) {

        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).get();
        List<CartDetails> cartDetailsList = cart.getCartDetails();
        ProductDTO product = productService.getProduct(productId).getBody();
        int cartDetailsIndex = findCartDetailsIndex(productId, cartDetailsList);
        CartDetails cartDetails = cart.getCartDetails().get(cartDetailsIndex);
        int cartDetailsId = cartDetails.getId();
        cart.setTotalAmount(cart.getTotalAmount() - (product.getPrice() * cartDetails.getQuantity()));
        cart.getCartDetails().remove(cartDetailsIndex);
        cart.setCartDetails(cartDetailsList);
        Cart newCart = cartDao.save(cart);
        cartDetailDao.deleteById(cartDetailsId);
        List<CartItems> cartItemsList = new ArrayList<>();
        for(CartDetails details:newCart.getCartDetails()){
            ProductDTO productDTO = productService.getProduct(details.getProductId()).getBody();
            cartItemsList.add(convertToCartItems(details,productDTO));
        }
        return cartItemsList;
    }

    public String deleteAllCartDetails(User user) {

        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).get();
        cart.getCartDetails().clear();
        cart.setTotalAmount(0);
        cartDao.save(cart);
        return "Cart is clear";
    }

}
