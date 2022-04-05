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

import org.hibernate.annotations.GeneratorType;

import com.example.springapi.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
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