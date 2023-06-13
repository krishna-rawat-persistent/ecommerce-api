package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="User_Order")
@Builder
@Entity
public class UserOrder implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name="quantity")
    private int quantity;

    @Column(name="total_amount")
    private double totalAmount=0;

    @Column(name = "Status")
    private String status;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="tracking_order_id",unique = true)
    private UUID trackingOrderId;

    @Column(name="ordered_date")
    private LocalDate date;

    @Column(name="delivery_date")
    private LocalDate deliveryDate;

    @Column(name="Shipping_Charges")
    private double shippingCharges;
}

