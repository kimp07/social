package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.springframework.data.domain.Page;

@Mapper
public interface FriendshipRequestDtoMapper {

    @Mapping(target = "requestDate", source = "requestDate", dateFormat = "yyyy-MM-dd hh:mm:ss")
    FriendshipRequestDto fromEntity(FriendshipRequest entity);

    default Page<FriendshipRequestDto> map(Page<FriendshipRequest> entities) {
        return entities.map(this::fromEntity);
    }
}
