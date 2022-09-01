package com.example.springapi.security.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springapi.security.entity.User;
import com.example.springapi.uploadfile.model.FileDB;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private int id;
    
    private String name;

    private String email;

    private String username;

    private String address;

    private String rememberToken;

    private Date createdAt;

    private Date updatedAt;
    
   
    @JsonIgnore
    private String password;


    private Collection<? extends GrantedAuthority> authorities;
    
    private FileDB imageUser;
    private String tokenFireBase;

    private String phone;
//    public UserDetailsImpl(Long id, String username, String email, String password, String address,
//           Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.authorities = authorities; 
//    }
//    
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getAddress(),
                user.getRememberToken(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
               user.getPassword(),
               authorities,
               user.getImageUser(),
               user.getTokenFireBase(),
               user.getPhone());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return authorities;
    }
    public int getId() {
       return id;
    }
    public String getEmail() {
      return email;
    }
    @Override
    public String getPassword() {
       return password;
       
    }

    public String getPhone() {
       return phone;
       
    }

    @Override
    public String getUsername () {
       return username;
    }
    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


    
	@Override
	public boolean isEnabled() {
	    return true;
	}
	@Override
	public boolean equals(Object o) {
	   if (this == o)
	        return true;
	    if (o == null || getClass() != o.getClass())
	        return false;
	    UserDetailsImpl user = (UserDetailsImpl) o;
	    return Objects.equals(id, user.id);
}
}