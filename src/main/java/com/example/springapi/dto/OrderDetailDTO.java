package com.example.springapi.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private long orderId;
    private long productId;
    private int quantity;
    private float price;
    private float discount;
}
