package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.entity.Coupon;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.services.CartServiceImpl;
import com.workflow2.ecommerce.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CouponController {

    @Autowired
    CartServiceImpl cartService;
    @Autowired
    CouponService couponService;
    @PostMapping("/coupon")
    @PreAuthorize("hasRole('Admin')")
    public String saveCoupon(HttpServletRequest httpServletRequest, @RequestBody Coupon coupon)
    {
        User user = cartService.getUser(httpServletRequest);
        couponService.saveCoupon(user,coupon);
    }
}
