package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    @OneToMany(targetEntity = OrderdDetails.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "Order_id")
    private List<OrderdDetails> orderDetailsList = new ArrayList<>();
}
