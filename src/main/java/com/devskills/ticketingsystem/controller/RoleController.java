package com.devskills.ticketingsystem.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devskills.ticketingsystem.model.Role;
import com.devskills.ticketingsystem.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role")
public class RoleController {
	
	private final RoleService roleService;
	
	@Operation(
			description = "Get all roles",
			summary = "Get all existing roles from database",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					)
			}
	)
	@GetMapping("")
	public ResponseEntity< List<Role> > getRoles() {
		return ResponseEntity.ok().body(roleService.getRoles());
	}
	
	@Operation(
			description = "Get role by name",
			summary = "Get specific role by name",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					)
			}
	)
	@GetMapping("/{name}")
	public ResponseEntity<Role> getRole(@PathVariable("name") String name) {
		return ResponseEntity.ok().body(roleService.getRole(name));
	}
	
	@Operation(
			description = "Create role",
			summary = "Add new role in the database",
			responses = {
					@ApiResponse(
							description = "Created",
							responseCode = "201"
					)
			}
	)
	@PostMapping("")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/roles").toUriString());
		return ResponseEntity.created(uri).body(roleService.saveRole(role));
	}
	
	@Operation(
			description = "Update role",
			summary = "Update existing role in the database",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					)
			}
	)
	@PutMapping("/edit/{id}")
	public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @RequestBody Role role) {
		return ResponseEntity.ok().body(roleService.updateRole(id, role));
	}
	
	@Operation(
			description = "Delete role",
			summary = "Delete role by name",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					)
			}
	)
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<?> deleteRole(@PathVariable("name") String name) {
		roleService.deleteRole(name);
		return ResponseEntity.ok().build();
	}

}
