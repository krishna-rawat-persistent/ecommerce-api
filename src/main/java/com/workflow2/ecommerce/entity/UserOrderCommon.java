package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String orderId;

    private double totalAmount;
    private String address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userOrderCommon")
    private List<OrderDetails> orderDetailsList = new ArrayList<>();
}
