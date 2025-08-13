package com.skillforge.skillforge_api.service;


import com.skillforge.skillforge_api.dto.mapper.PermissionMapper;
import com.skillforge.skillforge_api.dto.response.PermissionDTO;
import com.skillforge.skillforge_api.entity.Permission;
import com.skillforge.skillforge_api.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

       private final PermissionRepository permissionRepository;
       private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Transactional
    public Permission createPermission(Permission permission) {
        Permission savedPermission = permissionRepository.save(permission);
        return savedPermission;
    }

}
