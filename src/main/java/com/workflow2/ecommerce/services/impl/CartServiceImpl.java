package com.workflow2.ecommerce.services.impl;

import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.CartRepo;
import com.workflow2.ecommerce.repository.UserRepo;
import com.workflow2.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;


    @Override
    public String add_to_cart(CartDetails cartDetails, UUID userId) {
        User user = userRepo.findById(userId).get();
        Cart cart= user.getCart();
        double total = cart.getTotalAmout();
        if(cartDetails.getQuantity()==0){
            cartDetails.setQuantity(1);
        }
        total = total + (cartDetails.getQuantity() * cartDetails.getPrice());
        cart.setTotalAmout(total);
        Integer cartId = cart.getUserCartId();
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
        cartRepo.save(cart);
        return "added to cart list data";
    }
}
