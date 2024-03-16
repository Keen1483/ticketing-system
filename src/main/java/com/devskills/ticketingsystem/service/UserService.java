package com.devskills.ticketingsystem.service;

import java.util.List;
import java.util.Set;

import com.devskills.ticketingsystem.model.Role;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;

public interface UserService extends TicketingSystemService<User> {
	List<Ticket> getTickets(Long id);
	User getUser(String username);
	User addRolesToUser(String username, Set<Role> roles);
	User checkPassword(String password, User user);
}
