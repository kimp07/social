package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.dto.TalkMemberDto;
import org.springframework.data.domain.Page;

@Mapper
public interface TalkMemberDtoMapper {

    TalkMemberDto fromEntity(TalkMember entity);

    default Page<TalkMemberDto> map(Page<TalkMember> entities) {
        return entities.map(this::fromEntity);
    }
}
