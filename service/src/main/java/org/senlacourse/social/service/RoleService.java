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
public class RoleService implements IRoleService {

    public static final String NOT_DEFINED_FOR_ID = "Role not defined for id=";
    private final RoleRepository roleRepository;
    private final RoleDtoMapper roleDtoMapper;
    private final NewRoleDtoMapper newRoleDtoMapper;

    private void validateRoleNotNull(Role role, String message) throws ObjectNotFoundException {
        if (role == null) {
            ObjectNotFoundException e = new ObjectNotFoundException(message);
            log.error(e);
            throw e;
        }
    }

    @Override
    public Optional<RoleDto> findById(Long id) throws ObjectNotFoundException {
        Role role = roleRepository.findById(id).orElse(null);
        validateRoleNotNull(role, NOT_DEFINED_FOR_ID + id);
        return  Optional.of(roleDtoMapper.fromEntity(role));
    }

    @Override
    public Optional<RoleDto> findByName(String roleName) throws ObjectNotFoundException {
        Role role = roleRepository.findByName(roleName).orElse(null);
        validateRoleNotNull(role, "Role not defined for roleName=" + roleName);
        return  Optional.of(roleDtoMapper.fromEntity(role));
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
        Role roleFromBase = roleRepository.findById(dto.getId()).orElse(null);
        validateRoleNotNull(roleFromBase, NOT_DEFINED_FOR_ID + dto.getId());
        assert roleFromBase != null;
        roleFromBase
                .setRoleName(dto.getRoleName())
                .setRoleDisabled(dto.getRoleDisabled());
        return roleDtoMapper.fromEntity(roleRepository.save(roleFromBase));
    }

    @Override
    public void deleteRole(RoleDto dto) throws ObjectNotFoundException {
        Role roleFromBase = roleRepository.findById(dto.getId()).orElse(null);
        validateRoleNotNull(roleFromBase, NOT_DEFINED_FOR_ID + dto.getId());
        assert roleFromBase != null;
        roleRepository.delete(roleFromBase);
    }
}
