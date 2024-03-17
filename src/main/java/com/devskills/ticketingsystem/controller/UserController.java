package com.devskills.ticketingsystem.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devskills.ticketingsystem.model.Role;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
	
	private final UserService userService;
	
	@Operation(
			description = "Get all users",
			summary = "Get all existing users from database",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					)
			}
	)
	@GetMapping("")
	public ResponseEntity< List<User> > getUsers() {
		return ResponseEntity.ok().body(userService.getAll());
	}
	
	@Operation(
			description = "Get user tickets",
			summary = "Get all tickets assign to specific user",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					),
					@ApiResponse(
							description = "Not Found",
							responseCode = "404"
					)
			}
	)
	@GetMapping("/{id}/ticket")
	public ResponseEntity< List<Ticket> > getUserTickets(@PathVariable("id") Long id) {
		return ResponseEntity.ok().body(userService.getTickets(id));
	}
	
	@Operation(
			description = "Create User",
			summary = "Add new user in the database",
			responses = {
					@ApiResponse(
							description = "Created",
							responseCode = "201"
					),
					@ApiResponse(
							description = "Bad Request",
							responseCode = "400"
					)
			}
	)
	@PostMapping("")
	public ResponseEntity<User> CreateUser(@RequestBody User user) {
		System.out.println(user);
		//URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
		//return ResponseEntity.created(uri).body(userService.save(user));
		return ResponseEntity.created(null).body(userService.save(user));
	}
	
	@Operation(
			description = "Update User",
			summary = "Update existing user in the database",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					),
					@ApiResponse(
							description = "Bad Request",
							responseCode = "400"
					)
			}
	)
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
		return ResponseEntity.ok().body(userService.update(id, user));
	}
	
	// REFRESH TOKEN
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		System.out.println(authorizationHeader);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				User user = userService.getUser(username);
				
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(FORBIDDEN.value());
				// response.sendError(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", exception.getMessage());
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}

}
