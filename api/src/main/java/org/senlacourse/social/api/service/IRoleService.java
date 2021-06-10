package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.domain.Role;
import org.senlacourse.social.dto.NewRoleDto;
import org.senlacourse.social.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService extends IService<Role> {

    RoleDto findById(Long id) throws ObjectNotFoundException;

    RoleDto findByRoleName(String roleName) throws ObjectNotFoundException;

    Page<RoleDto> findAll(Pageable pageable);

    RoleDto saveRole(NewRoleDto dto);

    RoleDto updateRole(RoleDto dto) throws ObjectNotFoundException;

    void deleteById(Long id) throws ObjectNotFoundException;
}
