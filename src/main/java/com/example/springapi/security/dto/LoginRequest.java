package com.example.springapi.security.dto;

public class LoginRequest {
	private String username;
	private String password;
	
	// create an empty login request object
	
	public LoginRequest() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	
	// create a login request object with full attributes
	
	
	
	
}
