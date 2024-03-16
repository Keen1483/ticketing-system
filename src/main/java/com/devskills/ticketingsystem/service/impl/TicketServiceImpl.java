package com.devskills.ticketingsystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devskills.ticketingsystem.enums.TicketStatus;
import com.devskills.ticketingsystem.exception.BadContentException;
import com.devskills.ticketingsystem.exception.ResourceNotFoundException;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.repository.TicketRepository;
import com.devskills.ticketingsystem.repository.UserRepository;
import com.devskills.ticketingsystem.service.TicketService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketServiceImpl implements TicketService {
	
	private final TicketRepository ticketRepo;
	private final UserRepository userRepo;
	
	@Override
	public List<Ticket> getAll() {
		log.info("Fetching all tickets");
		return ticketRepo.findAll();
	}

	@Override
	public Ticket save(Ticket ticket) {
		Optional<Ticket> optionalTicket = ticketRepo.findByTitle(ticket.getTitle());
		if (optionalTicket.isPresent()) {
			log.info("Ticket with title {} already exist", ticket.getTitle());
			throw new BadContentException("Ticket with title " + ticket.getTitle() + " already exist");
		} else {
			log.info("Saving new ticket {} in the database", ticket.getTitle());
			if (ticket.getStatus() == null) {
				ticket.setStatus(TicketStatus.PROGRESS);
			}
			return ticketRepo.save(ticket);
		}
	}

	@Override
	public Ticket update(Long id, Ticket ticket) {
		Optional<Ticket> optionalTicket = ticketRepo.findById(id);
		
		if (optionalTicket.isPresent()) {
			Ticket currentTicket = optionalTicket.get();
			log.info("Updating ticket: {}", currentTicket.getTitle());
			
			String title = ticket.getTitle();
			if (title != null && !title.equals(currentTicket.getTitle())) {
				currentTicket.setTitle(title);
			}
			
			String description = ticket.getDescription();
			if (description != null && !description.equals(currentTicket.getDescription())) {
				currentTicket.setDescription(description);
			}
			
			TicketStatus status = ticket.getStatus();
			if (status != null && !status.equals(currentTicket.getStatus())) {
				currentTicket.setStatus(status);
			}
			
			return ticketRepo.save(currentTicket);
		} else {
			log.error("Ticket {} not found in database", ticket.getTitle());
			throw new ResourceNotFoundException("Ticket " + ticket.getTitle() + " not found in database");
		}
	}
	
	@Override
	public Ticket getTicket(Long id) {
		log.info("Fetching post {}", id);
		Optional<Ticket> optionalTicket = ticketRepo.findById(id);
		
		if (optionalTicket.isPresent()) {
			return optionalTicket.get();
		} else {
			log.error("Ticket {} not found in the database", id);
			throw new ResourceNotFoundException("Post " + id + " not found in the database");
		}
	}
	
	@Override
	public Ticket assignTicket(Long id, Long userId) {
		Optional<Ticket> optionalTicket = ticketRepo.findById(id);
		
		if (optionalTicket.isPresent()) {
			Ticket ticket = optionalTicket.get();
			
			Optional<User> optionalUser = userRepo.findById(userId);
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				log.info("Assigning ticket: {} to user: {}", ticket.getTitle(), user.getUsername());
				
				ticket.setUser(user);
				return ticketRepo.save(ticket);
			} else {
				log.error("User {} not found in database", userId);
				throw new ResourceNotFoundException("User " + userId + " not found in database");
			}
		} else {
			log.error("Ticket {} not found in database", id);
			throw new ResourceNotFoundException("Ticket " + id + " not found in database");
		}
	}
	
	@Override
	public Boolean deleteTicket(Long id) {
		Optional<Ticket> optionalTicket = ticketRepo.findById(id);
		
		if (optionalTicket.isPresent()) {
			log.warn("Deleting ticket {} in the database", id);
			
			ticketRepo.deleteById(id);
			return ticketRepo.findById(id).isEmpty();
		} else {
			log.error("Ticket {} not found in the database", id);
			throw new ResourceNotFoundException("Ticket " + id + " not found in the database");
		}
	}

}
