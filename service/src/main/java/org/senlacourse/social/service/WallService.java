package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.service.IWallService;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.dto.WallDto;
import org.senlacourse.social.mapstruct.WallDtoMapper;
import org.senlacourse.social.repository.WallRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j
public class WallService extends AbstractService<Wall> implements IWallService {

    private final WallRepository wallRepository;
    private final WallDtoMapper wallDtoMapper;

    @Override
    public Wall findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                wallRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public WallDto findById(Long id) throws ObjectNotFoundException {
        return wallDtoMapper.fromEntity(findEntityById(id));
    }

    @Override
    public WallDto findWallBySocietyId(Long societyId) throws ObjectNotFoundException {
        return wallDtoMapper.fromEntity(
                validateEntityNotNull(
                        wallRepository
                                .findBySocietyId(societyId)
                                .orElse(null)));
    }

    @Override
    public WallDto findRootWall() throws ObjectNotFoundException {
        return wallDtoMapper.fromEntity(
                validateEntityNotNull(
                        wallRepository
                                .findRootWall()
                                .orElse(null)));
    }
}
