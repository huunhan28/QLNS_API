package com.example.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartForOrderDetail {
	private int userId;
    private int productId;
    private int quantity;
    private float price;
    private float discount;
}
