package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewUserDto;

@Mapper
public interface NewUserDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true, defaultValue = "true")
    @Mapping(target = "nonLocked", ignore = true, defaultValue = "true")
    @Mapping(target = "nonExpired", ignore = true, defaultValue = "true")
    @Mapping(target = "credentialsNonExpired", ignore = true, defaultValue = "true")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
    User toEntity(NewUserDto dto);
}
