package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.Cart;

import java.util.Optional;

public interface CartService {
    public Optional<Cart>getCart(int id);
    public Optional<Cart>getCartById();

}
