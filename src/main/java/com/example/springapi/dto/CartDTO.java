package com.example.springapi.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private int userId;
    private int productId;
    private int quantity;
}
