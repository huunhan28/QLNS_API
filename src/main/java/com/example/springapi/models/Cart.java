package com.example.springapi.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ForeignKey;
import com.example.springapi.security.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="cart")
// @IdClass(CartKey.class)
public class Cart {
    @EmbeddedId
	private CartKey id;
	// @ManyToOne
	// @JoinColumn(name="id")
	@ManyToOne
	// @MapsId("id")
	// @JoinColumn(
	// 		name = "user_id",
	// 		foreignKey = @ForeignKey(
	// 				name = "fk_cartuser"
	// 				)
	// 		) 
	@MapsId("userId")
	private User user;
	
	// @Id
	@ManyToOne
	// // @JoinColumn(name = "productId")
	// @ManyToOne
	// @MapsId("productId")
	// @JoinColumn(
	// 		name = "product_id",
	// 		foreignKey = @ForeignKey(
	// 				name = "fk_cartproduct"
	// 				)
	// 		) 
	@MapsId("productId")
	private Product product;

    private int quantity;
	
}
