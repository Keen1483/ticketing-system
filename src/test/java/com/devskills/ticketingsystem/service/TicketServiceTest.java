package com.devskills.ticketingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.devskills.ticketingsystem.enums.TicketStatus;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.repository.TicketRepository;
import com.devskills.ticketingsystem.repository.UserRepository;
import com.devskills.ticketingsystem.service.impl.TicketServiceImpl;
import com.devskills.ticketingsystem.service.impl.UserServiceImpl;

import jakarta.persistence.PersistenceException;

public class TicketServiceTest {
	
	// service we want to test
	@InjectMocks
	private TicketServiceImpl ticketService;
	@InjectMocks
	private UserServiceImpl userService;
	
	// declare the dependencies
	@Mock
	private TicketRepository ticketRepo;
	@Mock
	private UserRepository userRepo;
	
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
		for (Ticket ticket : tickets) {
			ticketService.save(ticket);
		}
		when(ticketRepo.findAll()).thenReturn(tickets);
		
		// When
		List<Ticket> obtainedTickets = ticketService.getAll();
		
		// Then
		assertEquals(tickets.size(), obtainedTickets.size());
		assertEquals(tickets.get(0).getTitle(), obtainedTickets.get(0).getTitle());
		assertEquals(tickets.get(0).getDescription(), obtainedTickets.get(0).getDescription());
		assertEquals(TicketStatus.PROGRESS, obtainedTickets.get(0).getStatus());
		
		assertEquals(tickets.get(1).getTitle(), obtainedTickets.get(1).getTitle());
		assertEquals(tickets.get(1).getDescription(), obtainedTickets.get(1).getDescription());
		assertEquals(tickets.get(1).getStatus(), obtainedTickets.get(1).getStatus());
	}
	
	@Test
	public void should_successfully_save_a_ticket() {
		// Given
		Ticket ticket = new Ticket("Ticket title", "Ticket description", null);
		
		// Mock the calls
		when(ticketRepo.save(ticket)).thenReturn(ticket);
		
		// When
		Ticket ticketSaved = ticketService.save(ticket);
		
		// Then
		assertEquals(ticket.getTitle(), ticketSaved.getTitle());
		assertEquals(ticket.getDescription(), ticketSaved.getDescription());
		assertEquals(TicketStatus.PROGRESS, ticketSaved.getStatus());
	}
	
	@Test
	public void should_successfully_update_a_ticket() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", null, null);
		
		when(ticketRepo.save(ticket)).thenReturn(ticket);
		
		Ticket ticketSaved = ticketService.save(ticket);
		
		assertEquals(ticket.getTitle(), ticketSaved.getTitle());
		assertEquals(null, ticketSaved.getDescription());
		assertEquals(TicketStatus.PROGRESS, ticketSaved.getStatus());
		
		// Mock the calls
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket));
		
		// When
		Ticket ticketUpdated = ticketService.update(1L, new Ticket("Ticket title", "Ticket description", TicketStatus.COMPLETED));
		
		// Then
		assertEquals(ticket.getTitle(), ticketUpdated.getTitle());
		assertEquals("Ticket description", ticketUpdated.getDescription());
		assertEquals(TicketStatus.COMPLETED, ticketUpdated.getStatus());
	}
	
	@Test
	public void should_successfully_get_a_ticket() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", "Ticket description", TicketStatus.CANCELLED);
		
		when(ticketRepo.save(ticket)).thenReturn(ticket);
		
		Ticket ticketSaved = ticketService.save(ticket);
		
		// Mock the calls
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticketSaved));
		
		// When
		Ticket obtainedTicket = ticketService.getTicket(1L);
		
		// Then
		assertEquals(ticket.getTitle(), obtainedTicket.getTitle());
		assertEquals(ticket.getDescription(), obtainedTicket.getDescription());
		assertEquals(ticket.getStatus(), obtainedTicket.getStatus());
	}
	
	@Test
	public void should_successfully_assign_ticket_to_user() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", "Ticket description", TicketStatus.CANCELLED);
		when(ticketRepo.save(ticket)).thenReturn(ticket);
		
		User user = new User(1L, "John", "john@mail.com");
		when(userRepo.save(user)).thenReturn(user);
		
		// Mock the calls
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket));
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		
		// When
		Ticket ticketAssigned = ticketService.assignTicket(1L, 1L);
		
		// Then
		assertEquals(user.getEmail(), ticketAssigned.getUser().getEmail());
		assertEquals(user.getUsername(), ticketAssigned.getUser().getUsername());
	}
	
	@Test
	public void should_successfully_delete_a_ticket() {
		// Given
		Ticket ticket = new Ticket(1L, "Ticket title", "Ticket description", null);
		
		when(ticketRepo.save(ticket)).thenReturn(ticket);
		
		Ticket ticketSaved = ticketService.save(ticket);
		
		assertEquals(ticket.getTitle(), ticketSaved.getTitle());
		assertEquals(ticket.getDescription(), ticketSaved.getDescription());
		assertEquals(TicketStatus.PROGRESS, ticketSaved.getStatus());
		
		// Mock the calls
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticketSaved));
		doThrow(new PersistenceException("Exception occured")).when(ticketRepo).deleteById(1L);
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticketSaved));
	}

}
