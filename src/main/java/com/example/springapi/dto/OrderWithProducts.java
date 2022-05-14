package com.example.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithProducts {
	private int id;
	private String productName;
	private int quantity;
	private float price;
	private float discount;
}
