package org.kushtrimhajrizi.rpalace.service.impl;

import org.kushtrimhajrizi.rpalace.entity.Permission;
import org.kushtrimhajrizi.rpalace.repository.PermissionRepository;
import org.kushtrimhajrizi.rpalace.security.PermissionType;
import org.kushtrimhajrizi.rpalace.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public Optional<Permission> findByPermissionType(PermissionType permissionType) {
        return permissionRepository.findByPermissionType(permissionType);
    }
}
