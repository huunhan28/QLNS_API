package com.example.springapi.models;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ForeignKey;
import javax.persistence.MapsId;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.springapi.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order")
public class Order  {

    @Id
    @Column(name = "order_id",insertable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
	@JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "create_at")
    private Date createAt;

    // @ManyToOne
	// @JoinColumn(name = "id")
    @ManyToOne
	@MapsId("id")
	@JoinColumn(
			name = "discount_id",
			foreignKey = @ForeignKey(
					name = "fk_discount"
					)
			) 
    private Discount discount;

    private String state;

    
}
