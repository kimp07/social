package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.dto.WallMessageDto;
import org.springframework.data.domain.Page;

@Mapper
public interface WallMessageDtoMapper {

    @Mapping(target = "messageDate", source = "messageDate", dateFormat = "yyyy-MM-dd hh:mm:ss")
    WallMessageDto fromEntity(WallMessage entity);

    default Page<WallMessageDto> map(Page<WallMessage> entities) {
        return entities.map(this::fromEntity);
    }
}
