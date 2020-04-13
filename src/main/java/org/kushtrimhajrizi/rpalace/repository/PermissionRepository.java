package org.kushtrimhajrizi.rpalace.repository;

import org.kushtrimhajrizi.rpalace.entity.Permission;
import org.kushtrimhajrizi.rpalace.security.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, PermissionType> {

    Optional<Permission> findByPermissionType(PermissionType permissionType);
}
