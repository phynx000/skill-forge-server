package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.response.PermissionDTO;
import com.skillforge.skillforge_api.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper
{
    public PermissionDTO toDto(Permission permission) {
        if (permission == null) {
            return null;
        }
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setApiPath(permission.getApiPath());
        dto.setMethod(permission.getMethod());
        dto.setModule(permission.getModule());
        return dto;
    }
}
