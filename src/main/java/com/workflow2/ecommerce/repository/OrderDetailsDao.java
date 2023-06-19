package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderDetailsDao extends JpaRepository<OrderDetails, UUID> {
}
