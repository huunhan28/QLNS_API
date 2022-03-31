package com.example.springapi.models;

import javax.persistence.Transient;

import java.util.Calendar;
import java.util.Collection;
import java.util.Objects;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private long productId;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

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

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	private Collection<Image> images;
	
}

