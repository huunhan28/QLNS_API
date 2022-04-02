package com.example.springapi.security.dto;

import java.util.Date;
import java.util.List;

import com.example.springapi.security.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
    
	private String name;

	private String email;

	private String username;

	private String address;

	private String rememberToken;

	private Date createdAt;

	private Date updatedAt;
	private List<Role> roles;
	public JwtResponse(String token, Long id, String name, String email, String username, String address,
			String rememberToken, Date createdAt, Date updatedAt, List<Role> roles) {
		this.token = token;
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.rememberToken = rememberToken;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.username = username;
		this.roles = roles;
	}

	

	// public JwtResponse(String accessToken, Long id, String username, String email, String address, List<String> roles) {
	// 	this.token = accessToken;
	// 	this.id = id;
	// 	this.username = username;
	// 	this.email = email;
	// 	this.roles = roles;
	// 	this.address = address;
	// }
}
