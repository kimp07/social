package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.UserImageId;
import org.senlacourse.social.dto.UserImageIdDto;
import org.springframework.data.domain.Page;

@Mapper
public interface UserImageIdDtoMapper {

    UserImageIdDto fromEntity(UserImageId entity);

    default Page<UserImageIdDto> map(Page<UserImageId> entities) {
        return entities.map(this::fromEntity);
    }
}
