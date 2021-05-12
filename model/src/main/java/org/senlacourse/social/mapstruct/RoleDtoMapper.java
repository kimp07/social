package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Role;
import org.senlacourse.social.dto.RoleDto;
import org.springframework.data.domain.Page;

@Mapper
public interface RoleDtoMapper {

    Role toEntity(RoleDto roleDto);

    RoleDto fromEntity(Role role);

    default Page<RoleDto> map(Page<Role> page) {
        return page.map(this::fromEntity);
    }
}
