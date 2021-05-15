package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.service.IRoleService;
import org.senlacourse.social.domain.Role;
import org.senlacourse.social.dto.NewRoleDto;
import org.senlacourse.social.dto.RoleDto;
import org.senlacourse.social.mapstruct.NewRoleDtoMapper;
import org.senlacourse.social.mapstruct.RoleDtoMapper;
import org.senlacourse.social.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class RoleService extends AbstractService<Role> implements IRoleService {

    public static final String NOT_DEFINED_FOR_ID = "Role not defined for id=";
    private final RoleRepository roleRepository;
    private final RoleDtoMapper roleDtoMapper;
    private final NewRoleDtoMapper newRoleDtoMapper;

    @Override
    Role findEntityById(Long id) throws ObjectNotFoundException {
        Role role = roleRepository.findById(id).orElse(null);
        return validateEntityNotNull(role, NOT_DEFINED_FOR_ID + id);
    }

    @Override
    public Optional<RoleDto> findById(Long id) throws ObjectNotFoundException {
        Role role = findEntityById(id);
        return Optional.of(roleDtoMapper.fromEntity(role));
    }

    @Override
    public Optional<RoleDto> findByName(String roleName) throws ObjectNotFoundException {
        Role role = roleRepository.findByName(roleName).orElse(null);
        validateEntityNotNull(role, "Role not defined for roleName=" + roleName);
        return Optional.of(roleDtoMapper.fromEntity(role));
    }

    @Override
    public Page<RoleDto> findAll(Pageable pageable) {
        return roleDtoMapper.map(roleRepository.findAll(pageable));
    }

    @Override
    public RoleDto saveRole(NewRoleDto dto) {
        return roleDtoMapper.fromEntity(
                roleRepository.save(
                        newRoleDtoMapper.toEntity(dto)));
    }

    @Override
    public RoleDto updateRole(@NotNull RoleDto dto) throws ObjectNotFoundException {
        Role roleFromBase = findEntityById(dto.getId());
        roleFromBase
                .setRoleName(dto.getRoleName())
                .setRoleDisabled(dto.getRoleDisabled());
        return roleDtoMapper.fromEntity(roleRepository.save(roleFromBase));
    }

    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        Role roleFromBase = findEntityById(id);
        roleRepository.deleteById(roleFromBase.getId());
    }
}