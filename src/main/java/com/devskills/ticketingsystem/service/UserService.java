package com.devskills.ticketingsystem.service;

import java.util.List;

import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;

public interface UserService extends TicketingSystemService<User> {
	List<Ticket> getTickets(Long id);
}
