package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.dto.FriendshipDto;

@Mapper
public interface FriendshipDtoMapper {

    FriendshipDto fromEntity(Friendship entity);
}
