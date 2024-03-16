package com.devskills.ticketingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.repository.TicketRepository;
import com.devskills.ticketingsystem.repository.UserRepository;
import com.devskills.ticketingsystem.service.impl.TicketServiceImpl;
import com.devskills.ticketingsystem.service.impl.UserServiceImpl;

public class UserServiceTest {
	
	// service we want to test
	@InjectMocks
	private UserServiceImpl userService;
	@InjectMocks
	private TicketServiceImpl ticketService;
	
	// declare the dependencies
	@Mock
	private UserRepository userRepo;
	@Mock
	private TicketRepository ticketRepo;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void should_successfully_get_all_users() {
		// Given
		User user1 = new User("John", "john@mail.com");
		User user2 = new User(null, "jane@mail.com");
		List<User> users = Arrays.asList(user1, user2);
		
		// Mock the calls
		for (User user : users) {
			userService.save(user);
		}
		when(userRepo.findAll()).thenReturn(users);
		
		// When
		List<User> obtainedUsers = userService.getAll();
		
		// Then
		assertEquals(users.size(), obtainedUsers.size());
		
		assertEquals(user1.getEmail(), obtainedUsers.get(0).getEmail());
		assertEquals(user1.getUsername(), obtainedUsers.get(0).getUsername());
		
		assertEquals(user2.getEmail(), obtainedUsers.get(1).getEmail());
		assertEquals("jane@mail.com", obtainedUsers.get(1).getUsername());
	}
	
	@Test
	public void should_successfully_save_a_user() {
		// Given
		User user = new User("John", "john@mail.com");
		
		// Mock the calls
		when(userRepo.save(user)).thenReturn(user);
		
		// When
		User userSaved = userService.save(user);
		
		// Then
		assertEquals(user.getUsername(), userSaved.getUsername());
		assertEquals(user.getEmail(), userSaved.getEmail());
	}
	
	@Test
	public void should_successfully_update_a_user() {
		// Given
		User user = new User(1L, null, "john@mail.com");
		
		when(userRepo.save(user)).thenReturn(user);
		
		User userSaved = userService.save(user);
		
		assertEquals("john@mail.com", userSaved.getUsername());
		assertEquals(user.getEmail(), userSaved.getEmail());
		
		// Mock the calls
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		
		// When
		User userUpdated = userService.update(1L, new User("John", "john@mail.com"));
		
		// Then
		assertEquals(userUpdated.getUsername(), "John");
		assertEquals(userUpdated.getEmail(), user.getEmail());
	}
	
	@Test
	public void should_successfully_get_user_tickets() {
		// Given
		Ticket ticket1 = new Ticket(1L, "Ticket title", "Ticket description", null);
		Ticket ticket2 = new Ticket(2L, "Ticket2 title", "Ticket2 description", null);
		when(ticketRepo.save(ticket1)).thenReturn(ticket1);
		when(ticketRepo.save(ticket2)).thenReturn(ticket2);
		
		User user = new User(1L, "John", "john@mail.com");
		when(userRepo.save(user)).thenReturn(user);
		
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket1));
		when(ticketRepo.findById(2L)).thenReturn(Optional.of(ticket2));
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		
		ticketService.assignTicket(1L, 1L);
		ticketService.assignTicket(2L, 1L);
		
		// Mock the calls
		when(ticketRepo.findByUser(user)).thenReturn(Arrays.asList(ticket1, ticket2));
		
		// When
		List<Ticket> userTickets = userService.getTickets(1L);
		
		// Then
		assertEquals(2, userTickets.size());
		
		assertEquals(user.getEmail(), userTickets.get(0).getUser().getEmail());
		assertEquals(user.getUsername(), userTickets.get(0).getUser().getUsername());
		
		assertEquals(user.getEmail(), userTickets.get(1).getUser().getEmail());
		assertEquals(user.getUsername(), userTickets.get(1).getUser().getUsername());
	}

}
