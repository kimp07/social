package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.dto.WallDto;

@Mapper
public interface WallDtoMapper {

    WallDto fromEntity(Wall entity);

}
