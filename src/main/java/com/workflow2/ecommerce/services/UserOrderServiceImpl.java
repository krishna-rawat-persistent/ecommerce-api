package com.workflow2.ecommerce.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.workflow2.ecommerce.dto.AllOrderDto;
import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.*;
import com.workflow2.ecommerce.repository.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class contains business logic for all user orders
 * @author Tejas_Badjate
 * @version v0.0.2
 */
@Service
public class UserOrderServiceImpl{

    @Autowired
    CartServiceImpl cartService;

    @Autowired
    UserOrderCommonDao userOrderCommonDao;

    @Autowired
    CartDao cartDao;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    UserDao userDao;

    /**
     * This method returns Index of OrderDetailsList for particular order details object
     * @param trackingId It is a tracking id for getting particular order
     * @param orderDetailsList It contains List of all orders
     * @return It returns index of particular order
     */
    public int getOrderDetailIndexUsingTrackingId(UUID trackingId, List<OrderDetails> orderDetailsList){
        int  orderIndex=0;
        for(int i=0;i<orderDetailsList.size();i++){
            if(orderDetailsList.get(i).getTrackingId().equals(trackingId)){
                orderIndex=i;
                break;
            }
        }
        return orderIndex;
    }

    /**
     * This method is used to place order
     * @param user  It is a user whose order we want to place
     * @param totalAmount It is total amount of that cart
     * @param address it is the address of that user
     * @return It returns a success message
     */
    public String placeOrder(User user, double totalAmount, String address) {
        List<CartItems> cartDetailsList =  cartService.getAllCartDetails(user);

        if(cartDetailsList.isEmpty()){
            return "There is no item in cart!!";
        }
        if(totalAmount==0){
            return "Total Amount should not be 0";
        }
        RazorpayClient razorpayClient;
        try {
            razorpayClient = new RazorpayClient("rzp_test_DHE0D98Cg7lMgY", "XT1mCZyC4dd8r1cjkp11mM7o");
        } catch (RazorpayException e) {
            return "Unable to make connection with Razorpay client";
        }

        JSONObject options = new JSONObject();
        options.put("amount", Math.round(totalAmount));
        options.put("currency", "INR");
        options.put("receipt", "txn_"+totalAmount);
        Order order;
        try {
             order = razorpayClient.orders.create(options);
        } catch (RazorpayException e) {
            return "Unable to create order because of "+e.getMessage();
        }
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        UserOrderCommon userOrderCommon1 = new UserOrderCommon();
        userOrderCommon1.setOrderId(order.get("id"));
        userOrderCommon1.setTotalAmount(totalAmount);

        UserOrderCommon userOrderCommon = userOrderCommonDao.save(userOrderCommon1);

        OrderDetails orderDetails;

        int cartDetailsIndex;
        for(cartDetailsIndex=0;cartDetailsIndex<cartDetailsList.size();cartDetailsIndex++){
            CartItems cartItems = cartDetailsList.get(cartDetailsIndex);
            ProductDTO product = productService.getProduct(cartItems.getProductId()).getBody();
            orderDetails = new OrderDetails();
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
            orderDetails.setTotalAmount((product != null ? product.getDiscountedPrice() : 0) +finalShippingCharges);
            orderDetails.setProductId(cartItems.getProductId());
            orderDetails.setUserOrderCommon(userOrderCommon);
            orderDetailsList.add(orderDetails);
            user.getUserOrders().add(userOrderCommon);
        }
        userOrderCommon.setOrderDetailsList(orderDetailsList);
        userOrderCommonDao.save(userOrderCommon);
        userOrderCommonDao.flush();
        userDao.save(user);
        Cart cart = cartDao.findById(user.getCart().getUserCartId()).get();
        cart.getCartDetails().clear();
        cartDao.save(cart);
        return "Success "+order.get("id").toString();
    }

    /**
     * This method is used to view all orders for a particular user
     * @param user It is a user who want to view their order
     * @return It returns list of all the orders for that particular user
     */
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
                ProductDTO product = productService.getProduct(orderDetails.getProductId()).getBody();
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

    /**
     * Using this method we can track order with the help of order id and tracking id
     * @param user It is the user whose order we wanted to track
     * @param orderId It is the order id for a particular order, generated while placing order
     * @param trackingId It is the tracking id for a particular order, generated while placing order
     * @return It return particular order whose order id and tracking id
     */
    public OrderDto trackOrder(User user, String orderId,UUID trackingId)
    {
        UserOrderCommon userOrderCommon = userOrderCommonDao.findById(orderId).get();
        List<OrderDetails> orderDetails = userOrderCommon.getOrderDetailsList();
        int index = getOrderDetailIndexUsingTrackingId(trackingId, orderDetails);
        OrderDetails userOrder = orderDetails.get(index);
        ProductDTO product = productService.getProduct(userOrder.getProductId()).getBody();
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