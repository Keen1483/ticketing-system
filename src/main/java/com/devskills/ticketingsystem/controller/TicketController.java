package com.devskills.ticketingsystem.controller;

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

import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket")
public class TicketController {
	
	private final TicketService ticketService;
	
	@Operation(
			description = "Get all tickets",
			summary = "Get all existing tickets from database",
			responses = {
					@ApiResponse(
							description = "Ok",
							responseCode = "200"
					)
			}
	)
	@GetMapping("")
	public ResponseEntity< List<Ticket> > getTickets() {
		return ResponseEntity.ok().body(ticketService.getAll());
	}
	
	@Operation(
			description = "Get ticket by id",
			summary = "Get one ticket by id",
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
	@GetMapping("/{id}")
	public ResponseEntity<Ticket> getTicket(@PathVariable("id") Long id) {
		return ResponseEntity.ok().body(ticketService.getTicket(id));
	}
	
	@Operation(
			description = "Create Ticket",
			summary = "Add new ticket in the database",
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
	public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
		//URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/tickets").toUriString());
		return ResponseEntity.created(null).body(ticketService.save(ticket));
	}
	
	@Operation(
			description = "Update Ticket",
			summary = "Update existing ticket in the database",
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
	public ResponseEntity<Ticket> updateTicket(@PathVariable("id") Long id, @RequestBody Ticket ticket) {
		return ResponseEntity.ok().body(ticketService.update(id, ticket));
	}
	
	@Operation(
			description = "Assign ticket to a user",
			summary = "Assign a specific ticket to a specific user",
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
	@PutMapping("/{id}/assign/{userId}")
	public ResponseEntity<Ticket> assignTicket(@PathVariable("id") Long id, @PathVariable("userId") Long useId) {
		return ResponseEntity.ok().body(ticketService.assignTicket(id, useId));
	}
	
	@Operation(
			description = "Delete User",
			summary = "Delete user by id",
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
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteTicket(@PathVariable("id") Long id) {
		return ResponseEntity.ok().body(ticketService.deleteTicket(id));
	}
	
}
