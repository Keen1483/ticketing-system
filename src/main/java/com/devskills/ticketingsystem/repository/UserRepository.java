package com.devskills.ticketingsystem.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devskills.ticketingsystem.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}
