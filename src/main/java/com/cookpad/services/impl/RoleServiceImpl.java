package com.cookpad.services.impl;

import com.cookpad.dto.RoleDto;
import com.cookpad.dto.UserDto;
import com.cookpad.entities.Role;
import com.cookpad.entities.User;
import com.cookpad.exceptions.ResourceNotFoundException;
import com.cookpad.repositories.RoleRepository;
import com.cookpad.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        log.info("roleDto: "  + roleDto);

        Role savedRole = roleRepository.save(new Role(roleDto.getRoleName()));
        return mapToDTO(savedRole);
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto roleDto) {
        log.info("roleId: " + roleId);
        log.info("roleDto: " + roleDto);

        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));

        existingRole.setRoleName(roleDto.getRoleName());

        Role updatedRole = roleRepository.save(existingRole);

        return mapToDTO(updatedRole);
    }

    @Override
    public void deleteRole(Long roleId) {
        log.info("roleId: "+ roleId);

        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role", "roleId", roleId);
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleDto mapToDTO(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

}
