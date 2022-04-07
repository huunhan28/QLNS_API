package com.example.springapi.models;

import java.util.Date;
import java.util.Collection;

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

import org.springframework.format.annotation.DateTimeFormat;

import com.example.springapi.uploadfile.model.FileDB;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount")
public class Discount {

    @Id
	@Column(name = "discount_id")
	private String id;

    private int quantity;

    private float percent;

    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;
    

    @ManyToOne
    @JoinColumn(name = "image_id")
    private FileDB imageDiscount;


	@Override
	public String toString() {
		return "Discount [id=" + id + ", quantity=" + quantity + ", values=" + percent + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", imageDiscount=" + imageDiscount + "]";
	}
    
 

    // @OneToMany(mappedBy = "discount", fetch = FetchType.EAGER)
	// private Collection<Order> order;
}
