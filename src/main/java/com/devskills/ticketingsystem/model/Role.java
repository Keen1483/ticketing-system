package com.devskills.ticketingsystem.model;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Roles", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Comparable<Role> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true, nullable=false)
	private String name;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private Collection<User> users = new ArrayList<>();
	
	@Override
	public int compareTo(Role role) {
		return this.getName().compareTo(role.getName());
	}

}
