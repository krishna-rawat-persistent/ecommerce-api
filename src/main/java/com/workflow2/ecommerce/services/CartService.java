package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.CartDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This Interface contains all the methods required to perform operations on cart
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@Service
public interface CartService {
    public String add_to_cart(CartDetails cartDetails, UUID userId);


}
