package com.devskills.ticketingsystem.repository;

import java.util.List;

import com.devskills.ticketingsystem.model.Role;

public interface RoleService {
	List<Role> getRoles();
	Role getRole(String name);
	Role saveRole(Role role);
	Role updateRole(Long id, Role role);
	void deleteRole(String name);
}
