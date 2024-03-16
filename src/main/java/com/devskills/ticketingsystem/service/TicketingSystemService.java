package com.devskills.ticketingsystem.service;

import java.util.List;

public interface TicketingSystemService<T> {
	List<T> getAll();
	T save(T obj);
	T update(Long id, T obj);
}
