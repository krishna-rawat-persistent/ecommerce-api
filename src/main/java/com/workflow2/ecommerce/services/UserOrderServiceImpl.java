package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.entity.*;
import com.workflow2.ecommerce.repository.CartDao;
import com.workflow2.ecommerce.repository.CartDetailDao;
import com.workflow2.ecommerce.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Service
public class UserOrderServiceImpl {
    @Autowired
    CartServiceImpl cartService;

    @Autowired
    UserDao userDao;


    @Autowired
    CartDao cartDao;

    @Autowired
    CartDetailDao cartDetailDao;

    public List<OrderDto> placeOrder(User user, double totalAmount, String address) {
        List<CartDetails> list = cartService.CartDeatils(user);
        int index;
        List<UserOrder> userOrders = new ArrayList<>();
        List<OrderDto> orderDtos = new ArrayList<>();
        for(index=0;index<list.size();index++){
            CartDetails cartDetails = list.get(index);
            UserOrder userOrder = new UserOrder();
            userOrder.setQuantity(cartDetails.getQuantity());
            userOrder.setProductId(cartDetails.getProductId());
            userOrder.setTotalAmount(totalAmount);
            userOrder.setStatus("Order Placed");
            userOrder.setTrackingOrderId(UUID.randomUUID());
            LocalDate date = LocalDate.now();
            userOrder.setDate(date);
            userOrder.setShippingCharges(cartDetails.getShippingCharges()/list.size());
            userOrder.setDeliveryDate(date.plusDays(7));
            if (user.getUserOrders().isEmpty()){
                userOrders.add(userOrder);
            }
            else{
                userOrders = user.getUserOrders();
                userOrders.add(userOrder);
            }


            orderDtos.add(OrderDto.builder()
                    .orderId(userOrder.getTrackingOrderId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .contactNo(user.getPhoneNo())
                    .orderTotal(totalAmount)
                    .quantity(userOrder.getQuantity())
                    .deliveryDate(userOrder.getDeliveryDate())
                    .status(userOrder.getStatus())
                    .deliveryAddress(address)
                    .productId(userOrder.getProductId())
                    .build());

        }
        user.setUserOrders(userOrders);
        userDao.save(user);
        Cart cart = cartDao.findById(user.getCart().getUserCartId()).get();
        cart.getCartDetails().clear();
        cart.setTotalAmout(0);
        cartDao.save(cart);
        return orderDtos;
    }


}
