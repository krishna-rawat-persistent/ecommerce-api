package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.AllOrderDto;
import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.entity.*;
import com.workflow2.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserOrderServiceImpl{

    @Autowired
    CartServiceImpl cartService;
    @Autowired
    UserOrderCommonDao userOrderCommonDao;

    @Autowired
    CartDao cartDao;

    @Autowired
    ProductDao productDao;
    @Autowired
    UserDao userDao;

    public int getIndexCartDetailsList( UUID trackingId, List<OrderDetails> list){
        int  index=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getTrackingId().equals(trackingId)){
                index=i;
                break;
            }
        }
        return index;
    }
    public String placeOrder(User user, double totalAmount, String address) {
        List<CartItems> cartDetailsList =  cartService.getAllCartDetails(user);
        List<UserOrderCommon> userOrderCommonList = new ArrayList<>();
        UserOrderCommon userOrderCommon = new UserOrderCommon();
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        userOrderCommon.setTotalAmount(totalAmount);
        int index;
        for(index=0;index<cartDetailsList.size();index++){
            CartItems cartItems = cartDetailsList.get(index);
            Product product = productDao.findById(cartItems.getProductId()).get();
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setAddress(address);
            orderDetails.setColor(cartItems.getColor());
            orderDetails.setSize(cartItems.getSize());
            orderDetails.setQuantity(cartItems.getQuantity());
            orderDetails.setStatus("Order Placed");
            double finalShippingCharges = cartItems.getShippingCharges()/cartDetailsList.size();
            orderDetails.setShippingCharges(finalShippingCharges);
            LocalDate date = LocalDate.now();
            orderDetails.setDate(date.toString());
            orderDetails.setDeliveryDate(date.plusDays(7).toString());
            orderDetails.setTotalAmount(product.getDiscountedPrice()+finalShippingCharges);
            orderDetails.setProductId(cartItems.getProductId());

            if (!userOrderCommon.getOrderDetailsList().isEmpty()) {
                orderDetailsList = userOrderCommon.getOrderDetailsList();
            }
            orderDetailsList.add(orderDetails);
            if(user.getUserOrders().isEmpty()){
                userOrderCommonList.add(userOrderCommon);
            }
            else {
                userOrderCommonList = user.getUserOrders();
                userOrderCommonList.add(userOrderCommon);
            }

        }
        userOrderCommon.setOrderDetailsList(orderDetailsList);
        user.setUserOrders(userOrderCommonList);
        userOrderCommonDao.save(userOrderCommon);
        userDao.save(user);
        Cart cart = cartDao.findById(user.getCart().getUserCartId()).get();
        cart.getCartDetails().clear();
        cartDao.save(cart);
        return "Success";
    }

    public List<AllOrderDto> viewAllOrders(User user){
        List<UserOrderCommon> userOrderCommonList = user.getUserOrders();
        List<AllOrderDto> allOrderDtoList = new ArrayList<>();
        int index;
        for(index=0;index<userOrderCommonList.size();index++){
            UserOrderCommon userOrderCommon = userOrderCommonList.get(index);
            int innerIndex;
            List<OrderDetails> orderDetailsList = userOrderCommon.getOrderDetailsList();
            for(innerIndex=0; innerIndex< orderDetailsList.size(); innerIndex++){
                OrderDetails orderDetails = orderDetailsList.get(innerIndex);
                Product product = productDao.findById(orderDetails.getProductId()).get();
                allOrderDtoList.add(AllOrderDto.builder()
                        .orderId(userOrderCommon.getOrderId())
                        .trackingId(orderDetails.getTrackingId())
                        .image(product.getImage())
                        .status(orderDetails.getStatus())
                        .orderedDate(orderDetails.getDate())
                        .deliveryDate(orderDetails.getDeliveryDate())
                        .size(orderDetails.getSize())
                        .color(orderDetails.getColor())
                        .description(product.getDescription())
                        .productName(product.getName())
                        .build());
            }
        }
        return allOrderDtoList;
    }

    public OrderDto trackOrder(User user, UUID orderId,UUID trackingId)
    {
        UserOrderCommon userOrderCommon = userOrderCommonDao.findById(orderId).get();
        List<OrderDetails> orderDetails = userOrderCommon.getOrderDetailsList();
        int index = getIndexCartDetailsList(trackingId, orderDetails);
        OrderDetails userOrder = orderDetails.get(index);
        Product product = productDao.findById(userOrder.getProductId()).get();
        return OrderDto.builder()
                .orderId(orderId)
                            .userName(user.getName())
                            .contactNo(user.getPhoneNo())
                            .email(user.getEmail())
                            .productId(userOrder.getProductId())
                            .productName(product.getName())
                            .productImage(product.getImage())
                            .quantity(userOrder.getQuantity())
                            .orderTotal(product.getDiscountedPrice()+userOrder.getShippingCharges())
                            .deliveryAddress(userOrder.getAddress())
                            .shippingCharges(userOrder.getShippingCharges())
                            .size(userOrder.getSize())
                            .status(userOrder.getStatus())
                            .deliveryDate(userOrder.getDeliveryDate())
                            .discountedPrice(product.getDiscountedPrice())
                            .orderedDate(userOrder.getDate())
                            .color(userOrder.getColor())
                            .description(product.getDescription())
                            .build();
    }
}