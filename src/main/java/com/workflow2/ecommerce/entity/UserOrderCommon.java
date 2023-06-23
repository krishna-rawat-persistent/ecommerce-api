package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is the entity class which contains relation between user and order
 * @author tejas_badjate
 * @version v0.0.2
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class UserOrderCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID OrderId;
    double totalAmount;

    @OneToMany(targetEntity = OrderDetails.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "Order_id")
    private List<OrderDetails> orderDetailsList = new ArrayList<>();
}
