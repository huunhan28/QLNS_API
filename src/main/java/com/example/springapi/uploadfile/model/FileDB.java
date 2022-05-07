package com.example.springapi.uploadfile.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.springapi.models.Category;
import com.example.springapi.models.Discount;
import com.example.springapi.models.Product;
import com.example.springapi.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="file_db")

public class FileDB{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @Lob
    @JsonIgnore
    private byte[]data;

    private String type;
    
    private String link;
    
    @JsonIgnore
    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
    private Collection<Product> products;

    @JsonIgnore
    @OneToMany(mappedBy = "imageDiscount", fetch = FetchType.LAZY)
    private Collection<Discount> discounts;
    
   
    
    @JsonIgnore
    @OneToMany(mappedBy = "imageCategory", fetch = FetchType.LAZY)
    private Collection<Category> categories;
    
    @JsonIgnore
    @OneToMany(mappedBy = "imageUser", fetch = FetchType.LAZY)
    private Collection<User> imageUser;
    
    public FileDB(String name, String type, byte[] data, String link) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.link = link;
    }
    
    public FileDB(Integer id, String name, String type, byte[] data, String link) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.link = link;
        this.id = id;
    }
    

}