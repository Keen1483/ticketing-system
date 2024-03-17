package com.devskills.ticketingsystem.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devskills.ticketingsystem.exception.BadContentException;
import com.devskills.ticketingsystem.exception.ResourceNotFoundException;
import com.devskills.ticketingsystem.model.Role;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.repository.RoleRepository;
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
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final UserRepository userRepo;
	private final TicketRepository ticketRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public List<User> getAll() {
		log.info("Fetching all users");
		return userRepo.findAll();
	}

	@Override
	public User save(User user) {
		System.out.println(user);
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
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.getRoles().add(roleRepo.findByName("ROLE_USER").get());
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
	
	@Override
	public User getUser(String username) {
		log.info("Fetching user {}", username);
		Optional<User> optionalUser = userRepo.findByUsername(username);
		
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			log.error("User {} not found in the database", username);
			throw new ResourceNotFoundException("User " + username + " not found in the database");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepo.findByUsername(username);
		if (optionalUser.isPresent()) {
			log.info("User found in the database: {}", username);
		} else {
			log.error("User {} not found in the database", username);
			throw new UsernameNotFoundException("User not found in the database");
		}
		
		User user = optionalUser.get();
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

	@Override
	public User addRolesToUser(String username, Set<Role> roles) {
		Optional<User> optionalUser = userRepo.findByUsername(username);
		
		if (optionalUser.isPresent() && !roles.isEmpty()) {
			User user = optionalUser.get();
			
			for (Role role : roles) {
				if (role != null && roleRepo.findByName(role.getName()).isPresent()) {
					log.info("Adding role {} to user {}", role.getName(), username);
					// Here, role ID can be null and role name can already exist in the database
					user.getRoles().add(roleRepo.findByName(role.getName()).get());
				} else {
					log.warn("Role {} not found in the database", role);
				}
			}
			return userRepo.save(user);
		} else {
			log.error("User {} not found in the database or roles collection {} is empty", username, roles);
			throw new ResourceNotFoundException("User " + username + " not found in the database or roles collection " + roles.toString() + " is empty");
		}
	}

	@Override
	public User checkPassword(String password, User user) {
		Optional<User> optionalUser = userRepo.findByUsername(user.getUsername());
		if (optionalUser.isPresent()) {
			User currentUser = optionalUser.get();
			if (passwordEncoder.matches(password, currentUser.getPassword())) {
				log.info("Password {} matches encoder password: {}", password, currentUser.getPassword());
				return currentUser;
			} else {
				log.error("{} is not the password of the user {}", password, user.getUsername());
				throw new ResourceNotFoundException(password + " is not the password of the user " + user.getUsername());
			}
		} else {
			log.error("User {} not found in the database", user.getUsername());
			throw new ResourceNotFoundException("User " + user.getUsername() + " not found in the database");
		}
	}

}
