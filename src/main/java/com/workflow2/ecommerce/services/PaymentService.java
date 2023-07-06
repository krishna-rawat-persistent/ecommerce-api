package com.workflow2.ecommerce.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String startPayment(double amount){
        if(amount==0){
            return "Total Amount should not be 0";
        }
        RazorpayClient razorpayClient;
        try {
            razorpayClient = new RazorpayClient("rzp_test_DHE0D98Cg7lMgY", "XT1mCZyC4dd8r1cjkp11mM7o");
        } catch (RazorpayException e) {
            return "Unable to make connection with Razorpay client";
        }
        JSONObject options = new JSONObject();
        options.put("amount", Math.round(amount));
        options.put("currency", "INR");
        options.put("receipt", "txn_"+amount);
        Order order;
        try {
            order = razorpayClient.orders.create(options);
        } catch (RazorpayException e) {
            return "Unable to create order because of "+e.getMessage();
        }

        return "Success "+order.get("id");
    }
}
