package com.devskills.ticketingsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.devskills.ticketingsystem.model.Role;
import com.devskills.ticketingsystem.model.Ticket;
import com.devskills.ticketingsystem.model.User;
import com.devskills.ticketingsystem.service.RoleService;
import com.devskills.ticketingsystem.service.TicketService;
import com.devskills.ticketingsystem.service.UserService;

@SpringBootApplication
public class TicketingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingSystemApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService, TicketService ticketService) {
		return args -> {
			roleService.saveRole(new Role(null, "user", null));
			roleService.saveRole(new Role(null, "admin", null));
			
			userService.save(new User(null, "John", "john@mail.com", "1234"));
			userService.save(new User(null, "Jane", "jane@mail.com", "1234"));
			
			ticketService.save(new Ticket(null, "Ticket 1", "Description ticket 1", null));
			ticketService.save(new Ticket(null, "Ticket 2", "Description ticket 2", null));
			ticketService.assignTicket(1L, 1L);
			ticketService.assignTicket(2L, 2L);
		};
	}

}
