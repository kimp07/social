package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.TalkMemberId;
import org.senlacourse.social.dto.TalkMemberIdDto;
import org.springframework.data.domain.Page;

@Mapper
public interface TalkMemberIdDtoMapper {

    TalkMemberIdDto fromEntity(TalkMemberId talkMember);

    default Page<TalkMemberIdDto> map(Page<TalkMemberId> entities) {
        return entities.map(this::fromEntity);
    }
}
