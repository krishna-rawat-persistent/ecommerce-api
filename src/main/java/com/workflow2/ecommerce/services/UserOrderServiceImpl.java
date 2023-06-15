package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.AllOrderDto;
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

    public int getIndexCartDetailsList( UUID trackingId, List<OrderdDetails> list){
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
        List<CartDetails> cartDetailsList =  cartService.CartDeatils(user);
        List<UserOrderCommon> userOrderCommonList = new ArrayList<>();
        UserOrderCommon userOrderCommon = new UserOrderCommon();
        List<OrderdDetails> orderdDetailsList = new ArrayList<>();

        userOrderCommon.setTotalAmount(totalAmount);
        int index;
        for(index=0;index<cartDetailsList.size();index++){
            CartDetails cartDetails = cartDetailsList.get(index);
            Product product = productDao.findById(cartDetails.getProductId()).get();
            OrderdDetails orderdDetails = new OrderdDetails();
            orderdDetails.setAddress(address);
            orderdDetails.setColor(cartDetails.getColor());
            orderdDetails.setSize(cartDetails.getSize());
            orderdDetails.setQuantity(cartDetails.getQuantity());
            orderdDetails.setStatus("Order Placed");
            double finalShippingCharges = cartDetails.getShippingCharges()/cartDetailsList.size();
            orderdDetails.setShippingCharges(finalShippingCharges);
            LocalDate date = LocalDate.now();
            orderdDetails.setDate(date.toString());
            orderdDetails.setDeliveryDate(date.plusDays(7).toString());
            orderdDetails.setTotalAmount(product.getDiscountedPrice()+finalShippingCharges);
            orderdDetails.setProductId(cartDetails.getProductId());

            if (userOrderCommon.getOrderDetailsList().isEmpty())
            {
                orderdDetailsList.add(orderdDetails);
            }
            else{
                orderdDetailsList = userOrderCommon.getOrderDetailsList();
                orderdDetailsList.add(orderdDetails);
            }
            if(user.getUserOrders().isEmpty()){
                userOrderCommonList.add(userOrderCommon);
            }
            else {
                userOrderCommonList = user.getUserOrders();
                userOrderCommonList.add(userOrderCommon);
            }

        }
        userOrderCommon.setOrderDetailsList(orderdDetailsList);
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
            List<OrderdDetails> orderdDetailsList = userOrderCommon.getOrderDetailsList();
            for(innerIndex=0;innerIndex<orderdDetailsList.size();innerIndex++){
                OrderdDetails orderdDetails = orderdDetailsList.get(innerIndex);
                Product product = productDao.findById(orderdDetails.getProductId()).get();
                allOrderDtoList.add(AllOrderDto.builder()
                        .orderId(userOrderCommon.getOrderId())
                        .trackingId(orderdDetails.getTrackingId())
                        .image(product.getImage())
                        .status(orderdDetails.getStatus())
                        .orderedDate(orderdDetails.getDate())
                        .deliveryDate(orderdDetails.getDeliveryDate())
                        .size(orderdDetails.getSize())
                        .color(orderdDetails.getColor())
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
        List<OrderdDetails> orderdDetails = userOrderCommon.getOrderDetailsList();
        int index = getIndexCartDetailsList(trackingId,orderdDetails);
        OrderdDetails userOrder = orderdDetails.get(index);
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