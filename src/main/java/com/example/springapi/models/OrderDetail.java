package com.example.springapi.models;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_detail")
public class OrderDetail implements Serializable{
	
    @Id
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

    private int quantity;

    private float price;

    private float discount;


}
