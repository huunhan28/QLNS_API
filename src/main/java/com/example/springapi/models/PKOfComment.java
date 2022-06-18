package com.example.springapi.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PKOfComment implements Serializable{
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;
}
