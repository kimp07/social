package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.Role;
import org.senlacourse.social.dto.NewRoleDto;

@Mapper
public interface NewRoleDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roleDisabled", ignore = true, defaultValue = "false")
    Role toEntity(NewRoleDto dto);
}
