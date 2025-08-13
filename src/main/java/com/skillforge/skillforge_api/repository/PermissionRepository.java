package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
