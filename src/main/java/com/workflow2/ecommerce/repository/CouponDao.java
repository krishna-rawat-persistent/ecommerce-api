package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponDao extends JpaRepository<Coupon, UUID> {

}
