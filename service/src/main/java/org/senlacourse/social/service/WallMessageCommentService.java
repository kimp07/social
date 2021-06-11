package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.service.IWallMessageCommentService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.domain.Society;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.domain.WallMessageComment;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageCommentDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.WallMessageCommentDto;
import org.senlacourse.social.mapstruct.WallMessageCommentDtoMapper;
import org.senlacourse.social.repository.WallMessageCommentRepository;
import org.senlacourse.social.repository.WallMessageRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
@Log4j
public class WallMessageCommentService extends AbstractService<WallMessageComment> implements IWallMessageCommentService {

    private static final String USER_WITH_ID = "User with id=";

    private final WallMessageCommentRepository wallMessageCommentRepository;
    private final WallMessageRepository wallMessageRepository;
    private final IUserService userService;
    private final IWallMessageService wallMessageService;
    private final WallMessageCommentDtoMapper wallMessageCommentDtoMapper;
    private final ISocietyService societyService;

    @Override
    public WallMessageComment findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                wallMessageCommentRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public Page<WallMessageCommentDto> findAllByWallMessageId(Long wallMessageId, Pageable pageable) {
        return wallMessageCommentDtoMapper.map(
                wallMessageCommentRepository.findAllByWallMessageId(wallMessageId, pageable));
    }

    @AuthorizedUser
    @Override
    public void deleteAllByMessageId(UserIdDto dto, Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = wallMessageService.findEntityById(messageId);
        Society society = wallMessage.getWall().getSociety();
        if (society.getOwner().getId().equals(dto.getAuthorizedUserId())) {
            wallMessageCommentRepository.deleteAllByWallMessageId(messageId);
        }
    }

    @AuthorizedUser
    @Override
    public void deleteByCommentIdAndUserId(UserIdDto dto, Long wallMessageCommentId)
            throws ObjectNotFoundException, ServiceException {
        WallMessageComment wallMessageComment = findEntityById(wallMessageCommentId);
        User user = wallMessageComment.getUser();
        Society society = wallMessageComment.getWallMessage().getWall().getSociety();
        if (society.getOwner().getId().equals(dto.getAuthorizedUserId()) || user.getId().equals(dto.getAuthorizedUserId())) {
            wallMessageRepository.deleteById(wallMessageCommentId);
        }
    }

    private boolean userCanAddMessageComment(User user, Wall wall) {
        return wall.getRoot()
                || societyService.isUserMemberOfSociety(user.getId(), wall.getSociety().getId());
    }

    private boolean userCanEditMessage(User user, Wall wall, WallMessageComment wallMessageComment) {
        return userCanAddMessageComment(user, wall) && wallMessageComment.getUser().getId().equals(user.getId());
    }

    private WallMessageComment addNewWallMessageComment(WallMessage wallMessage, User user, Wall wall,
                                                        String message, WallMessageComment answeredComment)
            throws ServiceException {
        if (userCanAddMessageComment(user, wall)) {
            WallMessageComment wallMessageComment = new WallMessageComment()
                    .setWallMessage(wallMessage)
                    .setUser(user)
                    .setAnsweredComment(answeredComment);
            wallMessageComment
                    .setMessage(message)
                    .setLikesCount(0)
                    .setDislikesCount(0)
                    .setMessageDate(LocalDateTime.now());
            return wallMessageCommentRepository.save(wallMessageComment);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + user.getId() +
                            " can`t add comment to wall with id=" + wall.getId());
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public WallMessageCommentDto addNewWallMessageComment(NewWallMessageCommentDto dto)
            throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = wallMessageService.findEntityById(dto.getWallMessageId());
        WallMessageComment answeredMessage = null;
        if (dto.getAnsweredCommentId() != null && !dto.getAnsweredCommentId().equals(0L)) {
            answeredMessage = findEntityById(dto.getAnsweredCommentId());
        }
        User user = userService.findEntityById(dto.getUserId());
        Wall wall = wallMessage.getWall();
        return wallMessageCommentDtoMapper.fromEntity(
                addNewWallMessageComment(wallMessage, user, wall, dto.getMessage(), answeredMessage));
    }

    private WallMessageComment editWallMessageComment(User user, WallMessageComment wallMessageComment, String message)
            throws ObjectNotFoundException, ServiceException {
        Wall wall = wallMessageComment.getWallMessage().getWall();
        if (userCanEditMessage(user, wall, wallMessageComment)) {
            wallMessageComment.setMessage(message);
            return wallMessageCommentRepository.save(wallMessageComment);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + user.getId() +
                            " can`t edit comment id=" + wallMessageComment.getId());
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public WallMessageCommentDto editWallMessageComment(EditMessageDto dto)
            throws ObjectNotFoundException, ServiceException {
        WallMessageComment wallMessageComment = findEntityById(dto.getMessageId());
        User user = userService.findEntityById(dto.getUserId());
        return wallMessageCommentDtoMapper.fromEntity(
                editWallMessageComment(user, wallMessageComment, dto.getMessage()));
    }

    @Override
    public void addLikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessageComment wallMessageComment = findEntityById(messageId);
        int likesCount = wallMessageComment.getLikesCount();
        wallMessageComment.setLikesCount(++likesCount);
        wallMessageCommentRepository.save(wallMessageComment);
    }

    @Override
    public void addDislikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessageComment wallMessageComment = findEntityById(messageId);
        int dislikesCount = wallMessageComment.getDislikesCount();
        wallMessageComment.setLikesCount(++dislikesCount);
        wallMessageCommentRepository.save(wallMessageComment);
    }
}
