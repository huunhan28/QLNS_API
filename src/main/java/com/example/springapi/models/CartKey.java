package com.example.springapi.models;

import java.io.Serializable;

import com.example.springapi.security.entity.User;

public class CartKey implements Serializable{
    private User user;
    private Product product;
}
