package com.devskills.ticketingsystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devskills.ticketingsystem.exception.BadContentException;
import com.devskills.ticketingsystem.exception.ResourceNotFoundException;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.repository.TicketRepository;
import com.devskills.ticketingsystem.repository.UserRepository;
import com.devskills.ticketingsystem.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final TicketRepository ticketRepo;
	@Override
	public List<User> getAll() {
		log.info("Fetching all users");
		return userRepo.findAll();
	}

	@Override
	public User save(User user) {
		if (user.getEmail() == null) {
			throw new BadContentException("Please, provide email for this user: " + user);
		} else {
			String username = user.getUsername() == null ? user.getEmail() : user.getUsername();
			
			Optional<User> optionalUser = userRepo.findByUsername(username);
			if (optionalUser.isPresent() ||
				userRepo.findByEmail(user.getEmail()).isPresent()) {
				log.info("username {} or {} already exist", username, user.getEmail());
				throw new BadContentException("Provided username or email already exist");
			} else {
				user.setUsername(username);
				log.info("Saving new user {} in the database", user.getUsername());
				return userRepo.save(user);
			}
		}
	}

	@Override
	public User update(Long id, User user) {
		Optional<User> optionalUser = userRepo.findById(id);
		
		if (optionalUser.isPresent()) {
			User currentUser = optionalUser.get();
			log.info("Updating user: {}", currentUser.getUsername());
			
			String email = user.getEmail();
			if (email != null && !email.equals(currentUser.getEmail())) {
				currentUser.setEmail(email);
			}
			
			String username = user.getUsername();
			if (username != null && !username.equals(currentUser.getUsername())) {
				currentUser.setUsername(username);
			}
			
			return userRepo.save(currentUser);
		} else {
			log.error("User {} not found in database", user.getUsername());
			throw new ResourceNotFoundException("User " + user.getUsername() + " not found in database");
		}
	}
	
	@Override
	public List<Ticket> getTickets(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			log.info("Getting tickets of user: {}", user.getUsername());
			
			return ticketRepo.findByUser(user);
			
		} else {
			log.error("User with provided id {} not found in database", id);
			throw new ResourceNotFoundException("User id " + id + " not found in database");
		}
	}

}
