package com.devskills.ticketingsystem.model;

import static jakarta.persistence.GenerationType.AUTO;

import com.devskills.ticketingsystem.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import static jakarta.persistence.FetchType.EAGER;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ticket", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
	
	@Id
	@GeneratedValue(strategy=AUTO)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne(fetch = EAGER)
	private User user;
	
	private TicketStatus status;
	
	public Ticket(String title, String description, TicketStatus status) {
		this.title = title;
		this.description = description;
		this.status = status;
	}
	
	public Ticket(Long id, String title, String description, TicketStatus status) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.status = status;
	}
}
