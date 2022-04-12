package com.example.springapi.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OrderDetailKey implements Serializable{
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;
}
