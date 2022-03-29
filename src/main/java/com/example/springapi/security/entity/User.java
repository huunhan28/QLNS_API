package com.example.springapi.security.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
        private String name;
    
        private String email;
    
        @Column(name = "phone_number")
        private String phoneNumber;
    
        private String address;
    
        private String password;
    
        @Column(name = "remember_token")
        private String rememberToken;
    
        @Column(name = "created_at")
        private Date createdAt;
    
        @Column(name = "updated_at")
        private Date updatedAt;
    
        private String username;
    
    @ManyToMany (fetch = FetchType. LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
                   inverseJoinColumns = @JoinColumn (name = "role_id"))
    private Set<Role> roles = new HashSet<>();
//   public User() {
//    }
//    public User (String username, String email, String password, String address) {
//        this.username = username;
//       this. email = email;
//        this.password = password;
//    }
    
    
   public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
public Long getId() {
        return id;
   }
   public void setId(Long id) {
        this.id = id;
   }
   public String getUsername () {
        return username;
   }
   public void setUsername (String username) {
        this.username = username;
   }
   public String getEmail() {
	   return email;
   }
	public void setEmail(String email) {
	   this. email = email;
	}
	public String getPassword() {
	   return password;
	}
	public void setPassword(String password) {
	   this.password = password;
	}
	public Set<Role> getRoles() {
	   return roles;
	}
	public void setRoles (Set<Role> roles) {
	   this.roles = roles;
	}
	
  }
