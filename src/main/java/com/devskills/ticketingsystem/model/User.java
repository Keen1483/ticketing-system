package com.devskills.ticketingsystem.model;

import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.AUTO;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import static jakarta.persistence.FetchType.LAZY;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=AUTO)
	private Long id;
	
	@Column(unique=true)
	private String username;
	
	@Column(nullable = false, unique=true)
	private String email;
	
	@Column(nullable = true)
	@JsonIgnore
	@OneToMany(mappedBy = "user",
			orphanRemoval = true,
			fetch = LAZY)
	private Set<Ticket> tickets = new HashSet<>();
	
	public User(String username, String email) {
		this.username = username;
		this.email = email;
	}
	
	public User(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	public void assignTicket(Ticket ticket) {
		tickets.add(ticket);
	}
	
	public void removeTicket(Ticket ticket) {
		tickets.remove(ticket);
	}
}
