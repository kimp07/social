package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.UserDto;
import org.springframework.data.domain.Page;

@Mapper
public interface UserDtoMapper {

    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
    UserDto fromEntity(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
    User toEntity(UserDto dto);

    default Page<UserDto> map(Page<User> entities) {
        return entities.map(this::fromEntity);
    }
}
