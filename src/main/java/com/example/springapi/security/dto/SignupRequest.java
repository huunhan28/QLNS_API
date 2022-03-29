package com.example.springapi.security.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	 private Long id;
	    
     private String name;
 
     private String email;
 
     private String phoneNumber;
 
     private String address;
 
     private String password;
 
     private String rememberToken;
 
     private Date createdAt;
 
     private Date updatedAt;
 
     private String username;
	private Set<String> role;
//	public SignupRequest() {
//		super();
//	}
//	public SignupRequest(String username, String email, String address, String password, Set<String> role) {
//		super();
//		this.username = username;
//		this.email = email;
//		this.address = address;
//		this.password = password;
//		this.role = role;
//	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<String> getRole() {
		return role;
	}
	public void setRole(Set<String> role) {
		this.role = role;
	}
	
	
	
	
}
