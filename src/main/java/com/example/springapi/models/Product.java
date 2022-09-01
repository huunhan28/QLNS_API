package com.example.springapi.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.springapi.uploadfile.model.FileDB;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="Product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int productId;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	
	@ManyToOne
	@JoinColumn(name="image_id")
	private FileDB image;

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

	// @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	// private Collection<Image> images;

    // @OneToMany(
    //     mappedBy = "product",
    //     cascade = CascadeType.ALL,
    //     orphanRemoval = true
    // )
    // private List<Cart> carts = new ArrayList<>();





	public Product(int productId, Category category, String name, float price, String calculationUnit, int total,
			String description, String slug, boolean display, float rate, float discount, int id, String url, int year) {
		this.productId = productId;
		this.category = category;
		this.name = name;
		this.price = price;
		this.calculationUnit = calculationUnit;
		this.total = total;
		this.description = description;
		this.slug = slug;
		this.display = display;
		this.rate = rate;
		this.discount = discount;
		this.id = id;
		this.url = url;
		this.year = year;
	}

	
}

