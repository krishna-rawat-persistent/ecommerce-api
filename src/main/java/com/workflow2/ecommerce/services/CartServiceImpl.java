package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.Product;
import com.workflow2.ecommerce.entity.User;

import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.CartDetailDao;
import com.workflow2.ecommerce.repository.ProductDao;
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
    ProductDao productDao;

    public User getUser(HttpServletRequest httpServletRequest){
        String data = httpServletRequest.getHeader("Authorization");
        String token = data.substring(7);
        String name = JwtUtil.extractUsername(token);
        User user = userDao.findByEmail(name);
        return user;
    }

    public int getIndexCartDetailsList( UUID productId, List<CartDetails> list){
        int  x=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getProductId().equals(productId)){
                x=i;
                break;
            }
        }
        return x;
    }



    public String add_to_cart(CartDetails cartDetails, User user1) {
        UUID id = user1.getId();
        User user = userDao.findById(id).get();
        Cart cart= user.getCart();
        double total = cart.getTotalAmout();
        total = total + (cartDetails.getQuantity() * cartDetails.getPrice());
        cart.setTotalAmout(total);
        cartDetails.setShippingCharges(100);
        cartDetails.setQuantity(1);
        cartDetails.setPrice(productDao.findById(cartDetails.getProductId()).get().getPrice());
        if(cart.getCartDetails().isEmpty())
        {
            List<CartDetails> list = new ArrayList<>();
            list.add(cartDetails);
            cart.setCartDetails(list);
        }
        else{
            List<CartDetails> list = cart.getCartDetails();
            list.add(cartDetails);
        }
        cartDao.save(cart);
        return "added to cart list data";
    }

    public Optional<Cart> getallcart(User user){
        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        return Optional.of(cart);
    }

    public List<CartDetails> CartDeatils(User user){
        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        List<CartDetails> list = cart.getCartDetails();
        return list;
    }

    public CartDetails CartDeatilById(User user,UUID productId){
        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        List<CartDetails> list = cart.getCartDetails();
        int index= getIndexCartDetailsList(productId,list);
        return list.get(index);
    }

    public CartDetails updateCartDetails(User user, CartDetails cartDetails){

        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        UUID productId = cartDetails.getProductId();
        List<CartDetails> list = cart.getCartDetails();
        int index= getIndexCartDetailsList(productId,list);
        CartDetails cartDetails1 = cart.getCartDetails().get(index);
        cart.setTotalAmout(cart.getTotalAmout() -(cartDetails1.getPrice() * cartDetails1.getQuantity()));
        cartDetails1.setProductId(cartDetails.getProductId());
        cartDetails1.setPrice(cartDetails.getPrice());
        cartDetails1.setQuantity(cartDetails.getQuantity());
        cart.setTotalAmout(cart.getTotalAmout() + (cartDetails1.getPrice() * cartDetails1.getQuantity()));
        cartDao.save(cart);
        cartDetailDao.save(cartDetails1);
        return cartDetails1;
    }

    public List<CartDetails> CartDeatilDeleteById(User user,UUID productId){

        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        List<CartDetails> list = cart.getCartDetails();

        int index= getIndexCartDetailsList(productId,list);
        CartDetails cartDetails1 = cart.getCartDetails().get(index);
        int cartDetailsId = cartDetails1.getId();
        cart.setTotalAmout(cart.getTotalAmout() -(cartDetails1.getPrice() * cartDetails1.getQuantity()));
        cart.getCartDetails().remove(index);
        cart.setCartDetails(list);
        cartDao.save(cart);
        cartDetailDao.deleteById(cartDetailsId);
        return list;
    }

    public String deleteAllCart(User user){

        int id = user.getCart().getUserCartId();
        Cart cart = cartDao.findById(id).get();
        cart.getCartDetails().clear();
        cart.setTotalAmout(0);
        cartDao.save(cart);
        return "Cart is clear";
    }

}
