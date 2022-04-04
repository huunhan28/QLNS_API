package com.example.springapi.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.example.springapi.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CartKey implements Serializable{
    @Column(name = "user_id")
    private int userId;
    @Column(name = "product_id")
    private int productId;


   

    
}
