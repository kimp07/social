package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.Correspondence;
import org.senlacourse.social.dto.CorrespondenceDto;
import org.springframework.data.domain.Page;

@Mapper
public interface CorrespondenceDtoMapper {

    CorrespondenceDto fromEntity(Correspondence entity);

    default Page<CorrespondenceDto> map(Page<Correspondence> entities) {
        return entities.map(this::fromEntity);
    }

}
