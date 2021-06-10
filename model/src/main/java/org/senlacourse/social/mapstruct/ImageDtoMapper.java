package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Image;
import org.senlacourse.social.dto.ImageDto;
import org.springframework.data.domain.Page;

@Mapper
public interface ImageDtoMapper {

    ImageDto fromEntity(Image entity);

    default Page<ImageDto> map(Page<Image> entities) {
        return entities.map(this::fromEntity);
    }
}
