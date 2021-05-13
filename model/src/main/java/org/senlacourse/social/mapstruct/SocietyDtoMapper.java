package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Society;
import org.senlacourse.social.dto.SocietyDto;
import org.springframework.data.domain.Page;

@Mapper
public interface SocietyDtoMapper {

    SocietyDto fromEntity(Society society);

    default Page<SocietyDto> map(Page<Society> entities) {
        return entities.map(this::fromEntity);
    }
}
