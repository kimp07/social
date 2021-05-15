package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.WallMessageDto;
import org.senlacourse.social.mapstruct.WallMessageDtoMapper;
import org.senlacourse.social.repository.UserRepository;
import org.senlacourse.social.repository.WallMessageCommentRepository;
import org.senlacourse.social.repository.WallMessageRepository;
import org.senlacourse.social.repository.WallRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class WallMessageService extends AbstractService<WallMessage> implements IWallMessageService {

    private static final String USER_NOT_DEFINED_FOR_ID = "User not defined for id=";
    private static final String USER_WITH_ID = "User with id=";

    private final WallMessageRepository wallMessageRepository;
    private final WallMessageCommentRepository wallMessageCommentRepository;
    private final WallRepository wallRepository;
    private final UserRepository userRepository;
    private final ISocietyService societyService;
    private final WallMessageDtoMapper wallMessageDtoMapper;

    @Override
    WallMessage findEntityById(Long id) throws ObjectNotFoundException {
        WallMessage wallMessage = wallMessageRepository.findById(id).orElse(null);
        return validateEntityNotNull(wallMessage, "Wall message not defined for id=" + id);
    }

    private User getUserById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                userRepository.findById(id).orElse(null),
                USER_NOT_DEFINED_FOR_ID + id);
    }

    @Override
    public Page<WallMessageDto> findAllByWallId(Long wallId, Pageable pageable) {
        return wallMessageDtoMapper.map(
                wallMessageRepository.findAllByWallId(wallId, pageable));
    }

    @Override
    public void deleteAllMessagesByWallIdAndUserId(Long wallId, Long userId) throws ObjectNotFoundException {
        Wall wall = validateEntityNotNull(wallRepository.findById(wallId).orElse(null),
                "Wall not defined for id=" + wallId);
        User owner = wall.getSociety().getOwner();
        if (owner.getId().equals(userId)) {
            wallMessageRepository.deleteAllByWallId(wallId);
            wallMessageCommentRepository.deleteAllByWallId(wallId);
        }
    }

    @Override
    public void deleteWallMessageByIdAndUserId(Long wallMessageId, Long userId)
            throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = findEntityById(wallMessageId);
        User user = wallMessage.getUser();
        Wall wall = wallMessage.getWall();
        if (user.getId().equals(userId) || wall.getSociety().getOwner().getId().equals(userId)) {
            wallMessageRepository.deleteById(wallMessageId);
            wallMessageCommentRepository.deleteAllByWallMessageId(wallMessageId);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + userId +
                            " can`t delete message with id=" + wallMessageId);
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    private boolean userCanAddMessage(User user, Wall wall) {
        return wall.getSociety() == null
                || societyService.isUserMemberOfSociety(user.getId(), wall.getSociety().getId());
    }

    private boolean userCanEditMessage(User user, Wall wall, WallMessage wallMessage) {
        return userCanAddMessage(user, wall) && wallMessage.getUser().getId().equals(user.getId());
    }

    private WallMessage addNewMessage(User user, Wall wall, String message) throws ServiceException {
        if (userCanAddMessage(user, wall)) {
            WallMessage wallMessage = new WallMessage();
            wallMessage.setWall(wall)
                    .setUser(user)
                    .setMessage(message)
                    .setMessageDate(LocalDateTime.now())
                    .setLikesCount(0)
                    .setDislikesCount(0);
            return wallMessageRepository.save(wallMessage);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + user.getId() +
                            " can`t add message to wall with id=" + wall.getId());
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<WallMessageDto> addNewMessge(NewWallMessageDto dto) throws ObjectNotFoundException, ServiceException {
        User user = getUserById(dto.getUserId());
        Wall wall = validateEntityNotNull(
                wallRepository.findById(dto.getWallId()).orElse(null),
                "Wall not defined for id=" + dto.getWallId());
        return Optional.ofNullable(
                wallMessageDtoMapper
                        .fromEntity(
                                addNewMessage(user, wall, dto.getMessage())));
    }

    private WallMessage editWallMessage(User user, WallMessage wallMessage, String message)
            throws ServiceException {
        Wall wall = wallMessage.getWall();
        if (userCanEditMessage(user, wall, wallMessage)) {
            wallMessage.setMessage(message);
            return wallMessageRepository.save(wallMessage);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + user.getId() +
                            " can`t edit message with id=" + wall.getId());
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<WallMessageDto> editWallMessage(EditMessageDto dto) throws ObjectNotFoundException, ServiceException {
        User user = getUserById(dto.getUserId());
        WallMessage wallMessage = findEntityById(dto.getMessageId());
        return Optional.ofNullable(
                wallMessageDtoMapper.fromEntity(
                        editWallMessage(user, wallMessage, dto.getMessage())));
    }

    @Override
    public void addLikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException {
        validateEntityNotNull(
                userRepository.findById(userId).orElse(null),
                USER_NOT_DEFINED_FOR_ID + userId);
        WallMessage wallMessage = findEntityById(messageId);
        int likesCount = wallMessage.getLikesCount();
        wallMessage.setLikesCount(++likesCount);
        wallMessageRepository.save(wallMessage);
    }

    @Override
    public void addDislikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException {
        validateEntityNotNull(
                userRepository.findById(userId).orElse(null),
                USER_NOT_DEFINED_FOR_ID + userId);
        WallMessage wallMessage = findEntityById(messageId);
        int dislikesCount = wallMessage.getDislikesCount();
        wallMessage.setLikesCount(++dislikesCount);
        wallMessageRepository.save(wallMessage);
    }
}
