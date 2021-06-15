package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.dto.FriendshipDto;
import org.springframework.data.domain.Page;

@Mapper
public interface FriendshipDtoMapper {

    FriendshipDto fromEntity(Friendship entity);

    default Page<FriendshipDto> map(Page<Friendship> entities) {
        return entities.map(this::fromEntity);
    }
}
