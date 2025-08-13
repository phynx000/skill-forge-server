package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.entity.Role;
import com.skillforge.skillforge_api.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
        private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role handleUpdateRole(Long id, Role role) {
        if (roleRepository.existsById(id)) {
            role.setId(id);
            return roleRepository.save(role);
        } else {
            throw new RuntimeException("Role not found with id: " + id);
        }
    }

    public Set<Role> getDefaultRolesForUser() {
        Set<Role> defaultRoles = roleRepository.findByNameIn(Set.of("STUDENT"));
        if (defaultRoles.isEmpty()) {
            throw new RuntimeException("Default roles not found");
        }
        return defaultRoles;
    }
}

