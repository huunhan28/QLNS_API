package com.example.springapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private int productId;
	private int categoryId;
	private String name;
	private float price;
	private String calculationUnit;
	private int total;
	private String description;
	private String slug;
	private boolean display;
	private float rate;
	private float discount;
	private int id;
	private String url;
	private int year;
}
