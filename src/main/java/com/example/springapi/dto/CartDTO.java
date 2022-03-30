package com.example.springapi.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private long userId;
    private long productId;
    private int quantity;
}
