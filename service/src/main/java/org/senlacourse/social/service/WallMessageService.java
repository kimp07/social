package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.dto.WallMessageDto;
import org.senlacourse.social.mapstruct.WallMessageDtoMapper;
import org.senlacourse.social.repository.WallMessageRepository;
import org.senlacourse.social.repository.WallRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class WallMessageService extends AbstractService<WallMessage> {

    private final WallMessageRepository wallMessageRepository;
    private final WallRepository wallRepository;
    private final WallMessageDtoMapper wallMessageDtoMapper;

    @Override
    WallMessage findEntityById(Long id) throws ObjectNotFoundException {
        WallMessage wallMessage = wallMessageRepository.findById(id).orElse(null);
        return validateEntityNotNull(wallMessage, "Wall message not defined for id=" + id);
    }

    public Page<WallMessageDto> findAllByWallId(Long wallId, Pageable pageable) {
        return wallMessageDtoMapper.map(
                wallMessageRepository.findAllByWallId(wallId, pageable));
    }

    public void deleteAllMessagesByWallIdAndUserId(Long wallId, Long userId) throws ObjectNotFoundException {
        Wall wall = validateEntityNotNull(wallRepository.findById(wallId).orElse(null),
                "Wall not defined for id=" + wallId);
        User owner = wall.getSociety().getOwner();
        if (owner.getId().equals(userId)) {
            wallMessageRepository.deleteAllByWallId(wallId);
            // TODO refactor here after WallMessageCommentsService
        }
    }

    public void deleteWallMessageByIdAndUserId(Long wallMessageId, Long userId) throws ObjectNotFoundException {
        WallMessage wallMessage = findEntityById(wallMessageId);
        User user = wallMessage.getUser();
        Wall wall = wallMessage.getWall();
        if (user.getId().equals(userId) || wall.getSociety().getOwner().getId().equals(userId)) {
            wallMessageRepository.deleteById(wallMessageId);
            // TODO refactor here after WallMessageCommentsService
        }
    }
}
