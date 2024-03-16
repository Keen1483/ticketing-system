package com.devskills.ticketingsystem.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Optional<Ticket> findByTitle(String title);
	List<Ticket> findByUser(User user);
}
