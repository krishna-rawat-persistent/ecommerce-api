package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment/{amount}")
    public ResponseEntity<String> startPayment(@PathVariable double amount){
        String msg = paymentService.startPayment(amount);
        if(msg.startsWith("Success")){
            return ResponseEntity.ok(msg.substring(8));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
