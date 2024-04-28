package com.cookpad.services;

import com.cookpad.dto.RoleDto;
import com.cookpad.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    RoleDto createRole(RoleDto roleDto);
    RoleDto updateRole(Long roleId, RoleDto roleDto);
    void deleteRole(Long roleId);

    RoleDto mapToDTO (Role role);
}
