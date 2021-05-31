package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.UserSimpleDto;
import org.springframework.data.domain.Page;

@Mapper
public interface UserSimpleDtoMapper {

    UserSimpleDto fromEntity(User entity);

    default Page<UserSimpleDto> map(Page<User> entities) {
        return entities.map(this::fromEntity);
    }
}
