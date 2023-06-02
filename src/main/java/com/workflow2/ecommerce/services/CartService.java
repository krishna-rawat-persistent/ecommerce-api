package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.CartDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CartService {
    public String add_to_cart(CartDetails cartDetails, UUID userId);


}
