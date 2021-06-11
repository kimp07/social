package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.TalkMember;
import org.springframework.data.domain.Page;

@Mapper
public interface TalkMemberIdDto {

    TalkMemberIdDto fromEntity(TalkMember talkMember);

    default Page<TalkMemberIdDto> map(Page<TalkMember> entities) {
        return entities.map(this::fromEntity);
    }
}
