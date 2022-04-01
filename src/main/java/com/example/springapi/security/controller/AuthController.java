package com.example.springapi.security.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.security.common.ERole;
import com.example.springapi.security.common.JwtUtils;
import com.example.springapi.security.dto.JwtResponse;
import com.example.springapi.security.dto.LoginRequest;
import com.example.springapi.security.dto.MessageResponse;
import com.example.springapi.security.dto.ResetPasswordRequest;
import com.example.springapi.security.dto.SignupRequest;
import com.example.springapi.security.entity.Role;
import com.example.springapi.security.entity.User;
import com.example.springapi.security.repository.RoleRepository;
import com.example.springapi.security.repository.UserRepository;
import com.example.springapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static Logger logger = Logger.getLogger(UserRepository.class.getName());
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
	public ResponseEntity<ResponseObject> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

		UsernamePasswordAuthenticationToken signin = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
				loginRequest.getPassword());

		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(signin);
		} catch (Exception e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.UNAUTHORIZED, "Failed", "Sign in failed! Check your account!", null);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtutils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return AppUtils.returnJS(HttpStatus.OK, "Ok", "Sign in successfully!",
				new JwtResponse(jwt, userDetails.getId(), userDetails.getName(), userDetails.getEmail(),
						userDetails.getUsername(), userDetails.getAddress(), userDetails.getRememberToken(),
						userDetails.getCreatedAt(), userDetails.getUpdatedAt(),
						 roles));

	}

	@PostMapping("/signup")
	public ResponseEntity<ResponseObject> registerUser(@Validated @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed", "Username is already taken!", null);

		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed", "Email is already taken!", null);

		}
		// Create new user's account
		// Create new user's account
		User user = new User(
				signUpRequest.getId(),
				signUpRequest.getName(),
				signUpRequest.getEmail(),
				signUpRequest.getUsername(),
				signUpRequest.getAddress(),
				signUpRequest.getRememberToken(),
				signUpRequest.getCreatedAt(),
				signUpRequest.getUpdatedAt(),
				encoder.encode(signUpRequest.getPassword()));
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {// if not role then set default is user
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found. "));
			roles.add(userRole);
		} else {
			for (String role : strRoles) {
				role = role.toLowerCase();
				System.out.println("role register:" + role);
				switch (role) {
					case "user":// khi role là user
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found. "));
						roles.add(userRole);
						break;
					default:
						return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed", "Roles do not exists", null);

				}
			}

		}
		user.setRoles(roles);
		try {
			User userCreated = userRepository.save(user);
			return AppUtils.returnJS(HttpStatus.OK, "Ok", "Register user successfully!", userCreated);

		} catch (ConstraintViolationException e) {
			// Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
			// String errorMessage = "";
			// if (!violations.isEmpty()) {
			// StringBuilder builder = new StringBuilder();
			// for (ConstraintViolation<?> o : violations) {
			// builder.append(" Column: " + o.getPropertyPath() + " " + o.getMessage())
			// .append(System.getProperty("line.separator"));
			// }
			// errorMessage = builder.toString();
			// } else {
			// errorMessage = "ConstraintViolationException occured.";

			// }
			return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed", AppUtils.getExceptionSql(e), null);

		}
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<ResponseObject> resetPassword(
			@Validated @RequestBody ResetPasswordRequest resetPasswordRequest) {

		String username = resetPasswordRequest.getUsername();
		String oldPassword = resetPasswordRequest.getOldPassword();
		String newPassword = resetPasswordRequest.getNewPassword();
		String confirmPassword = resetPasswordRequest.getConfirmPassword();

		Optional<User> userOptional = userRepository.findByUsername(username);
		User user = null;
		if (userOptional.isPresent()) {
			user = userOptional.get();
			if (encoder.matches(oldPassword, user.getPassword())) {
				if (newPassword.equals(confirmPassword)) {
					try {
						user.setPassword(encoder.encode(newPassword));
						User userUpdated = userRepository.save(user);
						return AppUtils.returnJS(HttpStatus.OK, "Ok", "Update password successfully!", userUpdated);
					} catch (ConstraintViolationException e) {
						// TODO: handle exception
						return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed", "Update password failed!" +
								AppUtils.getExceptionSql(e), null);

					}
				}
			}
		}
		return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed", "User not exists", null);

	}
}