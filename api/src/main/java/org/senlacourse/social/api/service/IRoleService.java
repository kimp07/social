package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.dto.NewRoleDto;
import org.senlacourse.social.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface IRoleService {

    RoleDto findById(Long id) throws ObjectNotFoundException;

    RoleDto findByName(String roleName) throws ObjectNotFoundException;

    Page<RoleDto> findAll(Pageable pageable);

    RoleDto saveRole(NewRoleDto dto);

    RoleDto updateRole(@NotNull RoleDto dto) throws ObjectNotFoundException;

    void deleteById(Long id) throws ObjectNotFoundException;
}
