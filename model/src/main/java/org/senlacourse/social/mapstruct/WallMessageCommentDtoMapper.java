package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senlacourse.social.domain.WallMessageComment;
import org.senlacourse.social.dto.WallMessageCommentDto;
import org.springframework.data.domain.Page;

@Mapper
public interface WallMessageCommentDtoMapper {

    @Mapping(target = "messageDate", source = "messageDate", dateFormat = "yyyy-MM-dd hh:mm:ss")
    WallMessageCommentDto fromEntity(WallMessageComment entity);

    default Page<WallMessageCommentDto> map(Page<WallMessageComment> entities) {
        return entities.map(this::fromEntity);
    }
}
