package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.UserOrderCommon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserOrderCommonDao extends JpaRepository<UserOrderCommon, UUID> {
}
