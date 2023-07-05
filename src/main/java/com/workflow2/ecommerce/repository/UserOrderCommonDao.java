package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.UserOrderCommon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserOrderCommonDao extends JpaRepository<UserOrderCommon, String> {
}
