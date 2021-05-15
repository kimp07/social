package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.dto.TalkDto;
import org.springframework.data.domain.Page;

@Mapper
public interface TalkDtoMapper {

    TalkDto fromEntity(Talk entity);

    default Page<TalkDto> map(Page<Talk> entities) {
        return entities.map(this::fromEntity);
    }
}
