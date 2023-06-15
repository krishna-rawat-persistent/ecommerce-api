package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.UUID;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class OrderdDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Tracking_id")
    private UUID trackingId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name="quantity")
    private int quantity;

    @Column(name="total_amount")
    private double totalAmount=0;

    @Column(name = "Status")
    private String status;

    @Column(name="ordered_date")
    private String date;

    @Column(name="delivery_date")
    private String deliveryDate;

    @Column(name="Shipping_Charges")
    private double shippingCharges;

    @Column(name="address")
    private String address;

    @Column(name="color")
    private String color;

    @Column(name="size")
    private String size;

}
