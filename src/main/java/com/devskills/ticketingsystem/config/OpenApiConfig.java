package com.devskills.ticketingsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				contact = @Contact(
						name = "Bernard-Geraud Dongmo",
						email = "keenndjc@gmail.com",
						url = "https://bernard-geraud.web.app"
				),
				description = "OpenApi documentation for Ticketing System provides enpoints to make CRUD for User and Ticket objects, and assign tickets to users.",
				title = "OpenApi documentation for Ticketing System",
				version = "1.0"
		),
		servers = {
				@Server(
						description = "Local Invironment",
						url = "http://localhost:9000"
				)
		}
)
public class OpenApiConfig {

}
