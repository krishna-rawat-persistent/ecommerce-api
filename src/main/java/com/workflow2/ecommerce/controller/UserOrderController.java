package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.AllOrderDto;
import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.services.CartServiceImpl;
import com.workflow2.ecommerce.services.UserOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserOrderController{
    @Autowired
    CartServiceImpl cartService;
    @Autowired
    UserOrderServiceImpl userOrderService;

    @PostMapping("/placeorder/{totalAmount}/{address}")
    public String placeOrder(HttpServletRequest httpServletRequest, @PathVariable double totalAmount,@PathVariable String address){
        User user = cartService.getUser(httpServletRequest);
        String success = userOrderService.placeOrder(user,totalAmount,address);
        return  success;
    }

    @GetMapping("/vieworders")
    public List<AllOrderDto> viewOrders(HttpServletRequest httpServletRequest)
    {
        User user = cartService.getUser(httpServletRequest);
        return userOrderService.viewAllOrders(user);
    }

    @GetMapping("/trackorder/{orderId}/{trackingId}")
    public OrderDto trackOrder(HttpServletRequest httpServletRequest,@PathVariable UUID orderId,@PathVariable UUID trackingId){
        User user = cartService.getUser(httpServletRequest);
        return userOrderService.trackOrder(user,orderId,trackingId);
    }
}
