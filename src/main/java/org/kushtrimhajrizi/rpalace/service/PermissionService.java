package org.kushtrimhajrizi.rpalace.service;

import org.kushtrimhajrizi.rpalace.entity.Permission;
import org.kushtrimhajrizi.rpalace.security.PermissionType;

import java.util.Optional;

public interface PermissionService {

    Optional<Permission> findByPermissionType(PermissionType permissionType);
}
