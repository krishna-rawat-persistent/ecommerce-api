package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.entity.Coupon;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.CouponDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    @Autowired
    CouponDao couponDao;
    public String saveCoupon(User user, Coupon coupon){
        Coupon coupon1 = new Coupon();
        coupon1.setCouponName(coupon.getCouponName());
        coupon1.setDescription(coupon.getDescription());
        coupon1.setDiscountPercent(coupon.getDiscountPercent());
        couponDao.save(coupon1);
        return "Coupon added";
    }
}
