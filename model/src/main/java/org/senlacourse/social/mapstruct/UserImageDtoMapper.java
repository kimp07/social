package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.UserImage;
import org.senlacourse.social.dto.UserImageDto;
import org.springframework.data.domain.Page;

@Mapper
public interface UserImageDtoMapper {

    UserImageDto fromEntity(UserImage entity);

    default Page<UserImageDto> map(Page<UserImage> entities) {
        return entities.map(this::fromEntity);
    }
}
