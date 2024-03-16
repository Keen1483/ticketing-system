package com.devskills.ticketingsystem.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.service.TicketService;
import com.devskills.ticketingsystem.service.UserService;

public class UserControllerTest {
	
	// controller we want to test
	@InjectMocks
	private UserController userController;
	
	// declare the dependencies
	@Mock
	private UserService userService;
	@Mock
	private TicketService ticketService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void should_successfully_get_all_users() {
		// Given
		User user1 = new User("John", "john@mail.com");
		User user2 = new User("Jane", "jane@mail.com");
		List<User> users = Arrays.asList(user1, user2);
		
		// Mock the calls
		when(userService.getAll()).thenReturn(users);
		
		// when
		List<User> obtainedUsers = userController.getUsers().getBody();
		
		// Then
		assertEquals(users.size(), obtainedUsers.size());
		
		assertEquals(user1.getEmail(), obtainedUsers.get(0).getEmail());
		assertEquals(user1.getUsername(), obtainedUsers.get(0).getUsername());
		
		assertEquals(user2.getEmail(), obtainedUsers.get(1).getEmail());
		assertEquals(user2.getUsername(), obtainedUsers.get(1).getUsername());
	}
	
	@Test
	public void should_successfully_get_user_tickets() {
		// Given
		Ticket ticket1 = new Ticket(1L, "Ticket title", "Ticket description", null);
		Ticket ticket2 = new Ticket(2L, "Ticket2 title", "Ticket2 description", null);
		List<Ticket> userTickets = Arrays.asList(ticket1, ticket2);
		User user = new User(1L, "John", "john@mail.com");
		
		// Mock the calls
		when(ticketService.save(ticket1)).thenReturn(ticket1);
		when(ticketService.save(ticket2)).thenReturn(ticket2);
		when(userService.save(user)).thenReturn(user);
		when(ticketService.assignTicket(1L, 1L)).thenReturn(ticket1);
		when(ticketService.assignTicket(2L, 1L)).thenReturn(ticket1);
		when(userService.getTickets(1L)).thenReturn(userTickets);
		
		// when
		List<Ticket> ObtainedUserTicket = userController.getUserTickets(1L).getBody();
		
		// Then
		assertEquals(2, ObtainedUserTicket.size());
		
		assertEquals(ticket1.getTitle(), ObtainedUserTicket.get(0).getTitle());
		assertEquals(ticket1.getDescription(), ObtainedUserTicket.get(0).getDescription());
		assertEquals(ticket1.getStatus(), ObtainedUserTicket.get(0).getStatus());
		
		assertEquals(ticket2.getTitle(), ObtainedUserTicket.get(1).getTitle());
		assertEquals(ticket2.getDescription(), ObtainedUserTicket.get(1).getDescription());
		assertEquals(ticket2.getStatus(), ObtainedUserTicket.get(1).getStatus());
	}
	
	@Test
	public void should_successfully_create_a_user() {
		// Given
		User user = new User(1L, "John", "john@mail.com");
		
		// Mock the calls
		when(userService.save(user)).thenReturn(user);
		
		// When
		User userCreated = userController.CreateUser(user).getBody();
		
		// Then
		assertEquals(user.getUsername(), userCreated.getUsername());
		assertEquals(user.getEmail(), userCreated.getEmail());
	}
	
	@Test
	public void should_successfully_update_a_user() {
		// Given
		User user = new User(1L, null, "john@mail.com");
		
		when(userService.save(user)).thenReturn(user);
		
		User userSaved = userService.save(user);
		userSaved.setUsername("John");
		
		// Mock the calls
		when(userService.update(1L, new User("John", "john@mail.com"))).thenReturn(userSaved);
		
		// When
		User userUpdated = userController.updateUser(1L, new User("John", "john@mail.com")).getBody();
		
		// Then
		assertEquals(userUpdated.getUsername(), "John");
		assertEquals(userUpdated.getEmail(), userUpdated.getEmail());
	}

}
