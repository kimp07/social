package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.api.service.IWallService;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.WallMessageDto;
import org.senlacourse.social.mapstruct.WallMessageDtoMapper;
import org.senlacourse.social.repository.WallMessageCommentRepository;
import org.senlacourse.social.repository.WallMessageRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j
public class WallMessageService extends AbstractService<WallMessage> implements IWallMessageService {

    private static final String USER_WITH_ID = "User with id=";

    private final WallMessageRepository wallMessageRepository;
    private final WallMessageCommentRepository wallMessageCommentRepository;
    private final IUserService userService;
    private final IWallService wallService;
    private final ISocietyService societyService;
    private final WallMessageDtoMapper wallMessageDtoMapper;

    @Override
    public WallMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                wallMessageRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public Page<WallMessageDto> findAllByWallId(Long wallId, Pageable pageable) {
        return wallMessageDtoMapper.map(
                wallMessageRepository.findAllByWallId(wallId, pageable));
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void deleteAllMessagesByWallIdAndUserId(Long userId, Long wallId)
            throws ObjectNotFoundException, ServiceException {
        Wall wall = wallService.findEntityById(wallId);
        User owner = wall.getSociety().getOwner();
        if (owner.getId().equals(userId)) {
            wallMessageRepository.deleteAllByWallId(wallId);
            wallMessageCommentRepository.deleteAllByWallId(wallId);
        }
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void deleteByMessageIdAndUserId(Long userId, Long wallMessageId)
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

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public WallMessageDto addNewMessage(NewWallMessageDto dto) throws ObjectNotFoundException, ServiceException {
        User user = userService.findEntityById(dto.getUserId());
        Wall wall = wallService.findEntityById(dto.getWallId());
        return wallMessageDtoMapper
                        .fromEntity(
                                addNewMessage(user, wall, dto.getMessage()));
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

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public WallMessageDto editWallMessage(EditMessageDto dto) throws ObjectNotFoundException, ServiceException {
        User user = userService.findEntityById(dto.getUserId());
        WallMessage wallMessage = findEntityById(dto.getMessageId());
        return wallMessageDtoMapper.fromEntity(
                editWallMessage(user, wallMessage, dto.getMessage()));
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void addLikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = findEntityById(messageId);
        int likesCount = wallMessage.getLikesCount();
        wallMessage.setLikesCount(++likesCount);
        wallMessageRepository.save(wallMessage);
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void addDislikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = findEntityById(messageId);
        int dislikesCount = wallMessage.getDislikesCount();
        wallMessage.setLikesCount(++dislikesCount);
        wallMessageRepository.save(wallMessage);
    }
}
