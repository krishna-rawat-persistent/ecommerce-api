package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.entity.CartDetails;
import com.workflow2.ecommerce.entity.User;

import com.workflow2.ecommerce.services.CartServiceImpl;
import com.workflow2.ecommerce.services.UserOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class UserOrderController {

    @Autowired
    CartServiceImpl cartService;
    @Autowired
    UserOrderServiceImpl userOrderService;


    @PostMapping("/placeorder/{totalAmount}/{address}")
    public List<OrderDto> placeOrderAll(HttpServletRequest httpServletRequest, @PathVariable double totalAmount, @PathVariable String address)
    {
        User user = cartService.getUser(httpServletRequest);
        List<OrderDto> userOrders = userOrderService.placeOrder(user,totalAmount,address);
        return userOrders;
    }

}

