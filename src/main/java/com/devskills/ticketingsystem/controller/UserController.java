package com.devskills.ticketingsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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

}
