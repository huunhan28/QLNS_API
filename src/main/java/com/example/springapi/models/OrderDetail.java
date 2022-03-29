package com.example.springapi.models;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.ForeignKey;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_detail")@IdClass(OrderDetailKey.class)
public class OrderDetail{
	
    @Id
	// @ManyToOne
	// @JoinColumn(name="order_id")
	@ManyToOne
	@MapsId("id")
	@JoinColumn(
			name = "order_id",
			foreignKey = @ForeignKey(
					name = "fk_orderdetail_order"
					)
			) 
	private Order order;
	
	@Id
	// @ManyToOne
	// @JoinColumn(name = "product_id")
	@ManyToOne
	@MapsId("productId")
	@JoinColumn(
			name = "product_id",
			foreignKey = @ForeignKey(
					name = "fk_orderdetail_product"
					)
			)
	private Product product;

    private int quantity;

    private float price;

    private float discount;


}
