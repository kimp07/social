package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.FriendshipId;
import org.senlacourse.social.dto.FriendshipIdDto;

@Mapper
public interface FriendshipIdDtoMapper {

    FriendshipIdDto fromEntity(FriendshipId entity);
}
