package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.springframework.data.domain.Page;

@Mapper
public interface FriendshipRequestDtoMapper {

    FriendshipRequestDto fromEntity(FriendshipRequest entity);

    default Page<FriendshipRequestDto> map(Page<FriendshipRequest> entities) {
        return entities.map(this::fromEntity);
    }
}
