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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class WallService extends AbstractService<Wall> implements IWallService {

    private final WallRepository wallRepository;
    private final WallDtoMapper wallDtoMapper;

    @Override
    Wall findEntityById(Long id) throws ObjectNotFoundException {
        Wall wall = wallRepository.findById(id).orElse(null);
        return validateEntityNotNull(wall, "Wall not defined for id=" + id);
    }

    @Override
    public WallDto findById(Long id) throws ObjectNotFoundException {
        return wallDtoMapper.fromEntity(findEntityById(id));
    }

    @Override
    public WallDto findWallBySocietyId(Long societyId) throws ObjectNotFoundException {
        return wallDtoMapper.fromEntity(
                validateEntityNotNull(
                        wallRepository.findBySocietyId(societyId).orElse(null),
                        "Wall not defined for society id=" + societyId));
    }

    @Override
    public WallDto findRootWall() throws ObjectNotFoundException {
        return wallDtoMapper.fromEntity(
                validateEntityNotNull(
                        wallRepository.findRootWall().orElse(null),
                        "Root wall not defined"));
    }
}
