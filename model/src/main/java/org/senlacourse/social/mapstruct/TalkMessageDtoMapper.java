package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.dto.TalkMessageDto;
import org.springframework.data.domain.Page;

@Mapper
public interface TalkMessageDtoMapper {

    @Mapping(target = "messageDate", source = "messageDate", dateFormat = "yyyy-MM-dd hh:mm:ss")
    TalkMessageDto fromEntity(TalkMessage entity);

    default Page<TalkMessageDto> map(Page<TalkMessage> entities) {
        return entities.map(this::fromEntity);
    }
}
