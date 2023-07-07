package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.CartDetailsDTO;
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
import java.util.*;

/**
 * This class contains implementations of all the methods to perform operations on cart functionality
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

    /**
     * This method help us find user from the user from the request body
     * @param httpServletRequest It is the request body
     * @return It returns the user we have found from the token
     */
    public User getUser(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String name = JwtUtil.extractUsername(token);
        return userDao.findByEmail(name);
    }

    /**
     * This method help us find the index of a cartDetails whose product is give
     * @param productId It is the productId of a product whose cartDetails we wanted to find
     * @param cartDetailsList It is the list of all the cartDetails
     * @return It returns the index of cartDetails whose productId is provided
     */
    public int findCartDetailsIndex(UUID productId, List<CartDetails> cartDetailsList) {
        int cartDetailIndex = 0;
        for (int currentIndex = 0; currentIndex < cartDetailsList.size(); currentIndex++) {
            if (cartDetailsList.get(currentIndex).getProductId().equals(productId)) {
                cartDetailIndex = currentIndex;
                break;
            }
        }
        return cartDetailIndex;
    }

    /**
     * This method converts CartDetails object to CartItems
     * @param cartDetails  It is the CartDetails for a cart
     * @param product It is the productDTO object whose details is in cartDetails object
     * @return It returns CartItems object
     */
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

    /**
     * This method help us to add an item to cart
     * @param cartDetails It is the cartDetails for particular cart
     * @param user This parameter is the user in which we need to add an item
     * @return This returns a success message
     */
    public String addToCart(CartDetailsDTO cartDetails, User user) {
        Cart cart = user.getCart();
        List<CartDetails> cartDetailsList = cart.getCartDetails();
        ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();
        int cartDetailsIndex = findCartDetailsIndex(cartDetails.getProductId(), cartDetailsList);
        if(cartDetailsIndex<cartDetailsList.size()){
            CartDetails cartDetails1 = cartDetailsList.get(cartDetailsIndex);
            cartDetails1.setQuantity(cartDetails1.getQuantity()+cartDetails.getQuantity());
            cart.setTotalAmount(cart.getTotalAmount()+ (product != null ? product.getPrice() : 0)*cartDetails.getQuantity());
            cartDetailsList.remove(cartDetailsIndex);
            cartDetailsList.add(cartDetails1);
        }else {

            double totalAmount = cart.getTotalAmount();
            totalAmount += (cartDetails.getQuantity() * Objects.requireNonNull(product).getPrice());

            cart.setTotalAmount(totalAmount);
            if (cartDetails.getQuantity() == 0) {
                cartDetails.setQuantity(1);
            }
            if (cartDetails.getColor() == null) {
                cartDetails.setColor("#000000");
            }
            if (cartDetails.getSize() == null) {
                cartDetails.setColor("M");
            }
            if (cartDetails.getShippingCharges() == 0) {
                cartDetails.setShippingCharges(100);
            }

            CartDetails cartDetails1 = CartDetails.builder().productId(cartDetails.getProductId()).color(cartDetails.getColor()).size(cartDetails.getSize()).quantity(cartDetails.getQuantity()).shippingCharges(cartDetails.getShippingCharges()).build();
            cartDetailsList.add(cartDetails1);
        }
        cart.setCartDetails(cartDetailsList);

        cartDao.save(cart);
        return "Item Added to cart!!";
    }

    /**
     * This method returns cart for particular user
     * @param user It is the user whose cart we need to get
     * @return It returns Cart for given user
     */
    public Optional<Cart> getAllCart(User user) {
        int userId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(userId).orElse(null);
        return Optional.of(Objects.requireNonNull(cart));
    }

    /**
     * This method returns all cartItems for particular user
     * @param user It is the user whose cartItems we need to get
     * @return It returns list of CartItems for given user
     */
    public List<CartItems> getAllCartDetails(User user) {
        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).orElse(null);
        List<CartItems> cartItemsList = new ArrayList<>();
        for (CartDetails cartDetails : Objects.requireNonNull(cart).getCartDetails()) {
            ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();
            cartItemsList.add(convertToCartItems(cartDetails, Objects.requireNonNull(product)));
        }
        return cartItemsList;
    }

    /**
     * This method returns a cartItems for particular user
     * @param user It is the user whose cartItems we need to get
     * @param productId It is the productId of product that CartDetails contains
     * @return It returns a CartItems for given user
     */
    public CartItems getCartDetailsById(User user, UUID productId) {
        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).orElse(null);
        List<CartDetails> cartDetailsList = Objects.requireNonNull(cart).getCartDetails();
        int cartDetailsIndex = findCartDetailsIndex(productId, cartDetailsList);
        CartDetails cartDetails = cartDetailsList.get(cartDetailsIndex);
        ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();
        return convertToCartItems(cartDetails, Objects.requireNonNull(product));
    }

    /**
     * This method update CartDetails for the cart of provided user
     * @param user It is the user whose cartDetails we wanted to update
     * @param cartDetails It is the cartDetails that we need to update
     * @return It returns updated CartItems
     */
    public CartItems updateCartDetails(User user, CartDetails cartDetails) {

        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).orElse(null);
        UUID productId = cartDetails.getProductId();
        ProductDTO product = productService.getProduct(productId).getBody();
        List<CartDetails> cartDetailsList = Objects.requireNonNull(cart).getCartDetails();
        int cartDetailsIndex = findCartDetailsIndex(productId, cartDetailsList);

        CartDetails cartDetails1 = cart.getCartDetails().get(cartDetailsIndex);
        cart.setTotalAmount(cart.getTotalAmount() - (Objects.requireNonNull(product).getPrice() * cartDetails1.getQuantity()));
        cartDetails1.setProductId(cartDetails.getProductId());
        cartDetails1.setQuantity(cartDetails.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount() + (product.getPrice() * cartDetails1.getQuantity()));

        cartDao.save(cart);
        cartDetailDao.save(cartDetails1);
        return convertToCartItems(cartDetails1,product);
    }

    /**
     * This method delete cartDetails of product whose productId is given
     * @param user It is the user whose CartDetails we wanted to delete
     * @param productId It is the productId of a product that cartDetails contains
     * @return It returns updated list of CartItems after deleting that ProductDetails
     */
    public List<CartItems> deleteCartDetailsById(User user, UUID productId) {

        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).orElse(null);
        List<CartDetails> cartDetailsList = Objects.requireNonNull(cart).getCartDetails();
        ProductDTO product = productService.getProduct(productId).getBody();
        int cartDetailsIndex = findCartDetailsIndex(productId, cartDetailsList);
        CartDetails cartDetails = cart.getCartDetails().get(cartDetailsIndex);
        int cartDetailsId = cartDetails.getId();
        cart.setTotalAmount(cart.getTotalAmount() - (Objects.requireNonNull(product).getPrice() * cartDetails.getQuantity()));
        cart.getCartDetails().remove(cartDetailsIndex);
        cart.setCartDetails(cartDetailsList);
        Cart newCart = cartDao.save(cart);
        cartDetailDao.deleteById(cartDetailsId);
        List<CartItems> cartItemsList = new ArrayList<>();
        for(CartDetails details:newCart.getCartDetails()){
            ProductDTO productDTO = productService.getProduct(details.getProductId()).getBody();
            cartItemsList.add(convertToCartItems(details, Objects.requireNonNull(productDTO)));
        }
        return cartItemsList;
    }

    /**
     * This method has capability to delete all the CartItems from the Cart
     * @param user It is the user whose cart we are using to delete cartItem
     * @return It returns a success message
     */
    public String deleteAllCartDetails(User user) {

        int cartId = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(cartId).orElse(null);

        Objects.requireNonNull(cart).getCartDetails().clear();
        cart.setTotalAmount(0);
        cartDao.save(cart);
        return "Cart is clear";
    }

}
