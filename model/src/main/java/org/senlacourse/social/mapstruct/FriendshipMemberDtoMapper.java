package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.FriendshipMember;
import org.senlacourse.social.dto.FriendshipMemberDto;
import org.springframework.data.domain.Page;

@Mapper
public interface FriendshipMemberDtoMapper {

    FriendshipMemberDto fromEntity(FriendshipMember entity);

    default Page<FriendshipMemberDto> map(Page<FriendshipMember> entities) {
        return entities.map(this::fromEntity);
    }
}
