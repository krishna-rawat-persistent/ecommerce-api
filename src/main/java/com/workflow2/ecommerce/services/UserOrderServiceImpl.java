package com.workflow2.ecommerce.services;
import com.workflow2.ecommerce.dto.AllOrderDto;
import com.workflow2.ecommerce.dto.CartItems;
import com.workflow2.ecommerce.dto.OrderDto;
import com.workflow2.ecommerce.dto.ProductDTO;
import com.workflow2.ecommerce.entity.*;
import com.workflow2.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.*;

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
    ProductDao productDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CartDetailDao cartDetailDao;

    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

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
    public String placeOrder(User user, double totalAmount, String address, String orderId) {
        List<CartItems> cartDetailsList =  cartService.getAllCartDetails(user);

        if(cartDetailsList.isEmpty()){
            return "There is no item in cart!!";
        }
        if(totalAmount==0){
            return "Total Amount should not be 0";
        }
        for(CartItems cartItems:cartDetailsList){
            Product product=productDao.getReferenceById(cartItems.getProductId());
            if(product.getTotalStock() <= 0){
                return "Sorry, Product is Out of stock!";
            }
            else if(product.getTotalStock()>=cartItems.getQuantity()){
                product.setTotalStock(product.getTotalStock()-cartItems.getQuantity());
            }else{
                return ("Order quantity exceeds total stock! please decrease the quantity");
            }
            productDao.save(product);
        
        }
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        UserOrderCommon userOrderCommon1 = new UserOrderCommon();
        userOrderCommon1.setOrderId(orderId);
        userOrderCommon1.setAddress(address);
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
            orderDetails.setStatus(getRandomNumberUsingNextInt(1,6));
            double finalShippingCharges = cartItems.getShippingCharges()/cartDetailsList.size();
            orderDetails.setShippingCharges(finalShippingCharges);
            LocalDate date = LocalDate.now();
            orderDetails.setDate(date.toString());
            orderDetails.setDeliveryDate(date.plusDays(getRandomNumberUsingNextInt(3,16)).toString());
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
        List<CartDetails> cartDetailsList1 = cart.getCartDetails();
        List<Integer> cartDetailsIdList = new ArrayList<>();
        for (CartDetails cartDetails : cartDetailsList1) {
            cartDetailsIdList.add(cartDetails.getId());
        }
        cart.getCartDetails().clear();
        for (Integer integer : cartDetailsIdList) {
            cartDetailDao.deleteById(integer);
        }
        cartDao.save(cart);
        return "Success";
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
                        .image(Objects.requireNonNull(product).getImage())
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
     * @return It returns particular order whose order id and tracking id
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
                .productName(Objects.requireNonNull(product).getName())
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