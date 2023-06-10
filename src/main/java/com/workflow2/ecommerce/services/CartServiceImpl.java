package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.Cart;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;

import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class contains implementations of all the methods to perform operations on cart functionality
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@Service
public class CartServiceImpl  {


    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductServiceImpl productService;

    /**
     * This Method add item to the cart
     * @param cartDetails It takes cart details which have productId, quantity and price as attribute
     * @param userId It is userId to which this cart belongs to
     * @return It returns Success message as String
     */

    public String add_to_cart(CartDetails cartDetails, UUID userId) {
        User user = userDao.findById(userId).get();
        Cart cart= user.getCart();
        double total = cart.getTotalAmout();
        if(cartDetails.getQuantity()==0){
            cartDetails.setQuantity(1);
        }
        ProductDTO product = productService.getProduct(cartDetails.getProductId()).getBody();
        total = total + (cartDetails.getQuantity() * product.getPrice());
        cart.setTotalAmout(total);

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
}
