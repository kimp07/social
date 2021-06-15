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

@Service
@RequiredArgsConstructor
@Log4j
public class RoleService extends AbstractService<Role> implements IRoleService {

    private final RoleRepository roleRepository;
    private final RoleDtoMapper roleDtoMapper;
    private final NewRoleDtoMapper newRoleDtoMapper;

    @Override
    public Role findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                roleRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public RoleDto findById(Long id) throws ObjectNotFoundException {
        Role role = findEntityById(id);
        return roleDtoMapper.fromEntity(role);
    }

    @Override
    public RoleDto findByRoleName(String roleName) throws ObjectNotFoundException {
        return roleDtoMapper.fromEntity(
                validateEntityNotNull(
                        roleRepository
                                .findByRoleName(roleName)
                                .orElse(null)));
    }

    @Override
    public Page<RoleDto> findAll(Pageable pageable) {
        return roleDtoMapper.map(roleRepository.findAll(pageable));
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public RoleDto saveRole(NewRoleDto dto) {
        return roleDtoMapper.fromEntity(
                roleRepository.save(
                        newRoleDtoMapper.toEntity(dto)));
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public RoleDto updateRole(RoleDto dto) throws ObjectNotFoundException {
        Role roleFromBase = findEntityById(dto.getId());
        roleFromBase
                .setRoleName(dto.getRoleName())
                .setRoleDisabled(dto.getRoleDisabled());
        return roleDtoMapper.fromEntity(roleRepository.save(roleFromBase));
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        Role roleFromBase = findEntityById(id);
        roleRepository.deleteById(roleFromBase.getId());
    }
}
