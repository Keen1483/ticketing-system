package com.devskills.ticketingsystem.service;

import com.devskills.ticketingsystem.model.Ticket;

public interface TicketService extends TicketingSystemService<Ticket> {
	Ticket getTicket(Long id);
	Ticket assignTicket(Long id, Long userId);
	Boolean deleteTicket(Long id);
}
