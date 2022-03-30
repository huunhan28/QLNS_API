package com.example.springapi.security.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springapi.security.common.ERole;
import com.example.springapi.security.common.JwtUtils;
import com.example.springapi.security.dto.JwtResponse;
import com.example.springapi.security.dto.LoginRequest;
import com.example.springapi.security.dto.MessageResponse;
import com.example.springapi.security.dto.SignupRequest;
import com.example.springapi.security.entity.Role;
import com.example.springapi.security.entity.User;
import com.example.springapi.security.repository.RoleRepository;
import com.example.springapi.security.repository.UserRepository;
import com.example.springapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtutils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
						, loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtutils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(
				new JwtResponse(jwt
				, userDetails.getId()
				, userDetails.getName()
				, userDetails.getEmail()
				, userDetails.getPhoneNumber()
				, userDetails.getAddress()
				, userDetails.getRememberToken()
				, userDetails.getCreatedAt()
				, userDetails.getUpdatedAt()
				,userDetails.getUsername()
				, roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is alreadv in use!"));
		}
		// Create new user's account
		// Create new user's account
		User user = new User( 
				signUpRequest.getId(),
				signUpRequest.getName(),
				signUpRequest.getEmail(),
				signUpRequest.getPhoneNumber(),
				signUpRequest.getAddress(),
				signUpRequest.getRememberToken(),
				signUpRequest.getCreatedAt(),
				signUpRequest.getUpdatedAt(),
				signUpRequest.getUsername (),
				encoder.encode(signUpRequest.getPassword()));
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {// if not role then set default is user
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found. "));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found. "));
					roles.add(userRole);

				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

	}
}
