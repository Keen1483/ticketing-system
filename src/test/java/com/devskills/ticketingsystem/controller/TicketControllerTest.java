package com.devskills.ticketingsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.devskills.ticketingsystem.enums.TicketStatus;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.service.TicketService;
import com.devskills.ticketingsystem.service.UserService;

public class TicketControllerTest {
	
	// controller we want to test
	@InjectMocks
	private TicketController ticketController;
	
	// declare the dependencies
	@Mock
	private TicketService ticketService;
	@Mock
	private UserService userService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void should_successfully_get_all_tickets() {
		// Given
		Ticket ticket1 = new Ticket("Title ticket 2", "Description ticket 1", null);
		Ticket ticket2 = new Ticket("Title ticket 2", "Description ticket 1", TicketStatus.COMPLETED);
		List<Ticket> tickets = Arrays.asList(ticket1, ticket2);
		
		// Mock the calls
		when(ticketService.getAll()).thenReturn(tickets);
		
		// When
		List<Ticket> obtainedTickets = ticketController.getTickets().getBody();
		
		// Then
		assertEquals(tickets.size(), obtainedTickets.size());
		assertEquals(tickets.get(0).getTitle(), obtainedTickets.get(0).getTitle());
		assertEquals(tickets.get(0).getDescription(), obtainedTickets.get(0).getDescription());
		assertEquals(tickets.get(0).getStatus(), obtainedTickets.get(0).getStatus());
		
		assertEquals(tickets.get(1).getTitle(), obtainedTickets.get(1).getTitle());
		assertEquals(tickets.get(1).getDescription(), obtainedTickets.get(1).getDescription());
		assertEquals(tickets.get(1).getStatus(), obtainedTickets.get(1).getStatus());
	}
	
	@Test
	public void should_successfully_get_a_ticket() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", "Ticket description", null);
		
		// Mock the calls
		when(ticketService.getTicket(1L)).thenReturn(ticket);
		
		// When
		Ticket obtainedTicket = ticketController.getTicket(1L).getBody();
		
		// Then
		assertEquals(ticket.getTitle(), obtainedTicket.getTitle());
		assertEquals(ticket.getDescription(), obtainedTicket.getDescription());
		assertEquals(ticket.getStatus(), obtainedTicket.getStatus());
	}
	
	@Test
	public void should_successfully_create_a_ticket() {
		// Given
		Ticket ticket = new Ticket("Ticket title", "Ticket description", null);
		
		// Mock the calls
		when(ticketService.save(ticket)).thenReturn(ticket);
		
		// When
		Ticket ticketCreated = ticketController.createTicket(ticket).getBody();
		
		// Then
		assertEquals(ticket.getTitle(), ticketCreated.getTitle());
		assertEquals(ticket.getDescription(), ticketCreated.getDescription());
		assertEquals(ticket.getStatus(), ticketCreated.getStatus());
	}
	
	@Test
	public void should_successfully_update_a_ticket() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", null, null);
		
		when(ticketService.save(ticket)).thenReturn(ticket);
		
		Ticket ticketSaved = ticketService.save(ticket);
		ticketSaved.setDescription("Ticket description");
		ticketSaved.setStatus(TicketStatus.COMPLETED);
		
		// Mock the calls
		when(ticketService.update(1L, new Ticket("Ticket title", "Ticket description", TicketStatus.COMPLETED))).thenReturn(ticketSaved);
		
		// When
		Ticket ticketUpdated = ticketController.updateTicket(1L, new Ticket("Ticket title", "Ticket description", TicketStatus.COMPLETED)).getBody();
		
		// Then
		assertEquals(ticket.getTitle(), ticketUpdated.getTitle());
		assertEquals("Ticket description", ticketUpdated.getDescription());
		assertEquals(TicketStatus.COMPLETED, ticketUpdated.getStatus());
	}
	
	@Test
	public void should_successfully_assign_ticket_to_user() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", "Ticket description", TicketStatus.PROGRESS);
		User user = new User(1L, "John", "john@mail.com");
		
		// Mock the calls
		when(ticketService.assignTicket(1L, 1L)).thenReturn(ticket);
		ticket.setUser(user);
		
		// When
		Ticket ticketAssigned = ticketController.assignTicket(1L, 1L).getBody();
		
		// Then
		assertEquals(user.getEmail(), ticketAssigned.getUser().getEmail());
		assertEquals(user.getUsername(), ticketAssigned.getUser().getUsername());
	}
	
	@Test
	public void should_successfully_delete_a_ticket() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", "Ticket description", null);
		
		when(ticketService.save(ticket)).thenReturn(ticket);
		
		// Mock the calls
		when(ticketService.deleteTicket(1L)).thenReturn(true);
		
		// when
		
		Boolean isDeleted = ticketController.deleteTicket(1L).getBody();
		
		// then
		assertEquals(true, isDeleted);
	}

}
