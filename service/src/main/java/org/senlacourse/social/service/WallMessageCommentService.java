package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IWallMessageCommentService;
import org.senlacourse.social.domain.Society;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.Wall;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.domain.WallMessageComment;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageCommentDto;
import org.senlacourse.social.dto.WallMessageCommentDto;
import org.senlacourse.social.mapstruct.WallMessageCommentDtoMapper;
import org.senlacourse.social.repository.UserRepository;
import org.senlacourse.social.repository.WallMessageCommentRepository;
import org.senlacourse.social.repository.WallMessageRepository;
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
public class WallMessageCommentService extends AbstractService<WallMessageComment> implements IWallMessageCommentService {

    private static final String WALL_MESSAGE_NOT_DEFINED_FOR_ID = "Wall message not defined for id=";
    private static final String USER_NOT_DEFINED_FOR_ID = "User not defined for id=";
    private static final String USER_WITH_ID = "User with id=";

    private final WallMessageCommentRepository wallMessageCommentRepository;
    private final WallMessageRepository wallMessageRepository;
    private final UserRepository userRepository;
    private final WallMessageCommentDtoMapper wallMessageCommentDtoMapper;
    private final ISocietyService societyService;

    private WallMessage getWallMessageById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                wallMessageRepository.findById(id).orElse(null),
                WALL_MESSAGE_NOT_DEFINED_FOR_ID + id);
    }

    private User getUserById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(userRepository.findById(id).orElse(null),
                USER_NOT_DEFINED_FOR_ID + id);
    }

    @Override
    WallMessageComment findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                wallMessageCommentRepository.findById(id).orElse(null),
                "Wall message comment not defined for id=" + id);
    }

    @Override
    public Page<WallMessageCommentDto> findAllByWallMessageId(Long wallMessageId, Pageable pageable) {
        return wallMessageCommentDtoMapper.map(
                wallMessageCommentRepository.findAllByWallMessageId(wallMessageId, pageable));
    }

    @Override
    public void deleteAllByMessageId(Long messageId, Long userId) throws ObjectNotFoundException {
        WallMessage wallMessage = getWallMessageById(messageId);
        Society society = wallMessage.getWall().getSociety();
        if (society.getOwner().getId().equals(userId)) {
            wallMessageCommentRepository.deleteAllByWallMessageId(messageId);
        }
    }

    @Override
    public void delete(Long wallMessageCommentId, Long userId) throws ObjectNotFoundException {
        WallMessageComment wallMessageComment = findEntityById(wallMessageCommentId);
        User user = wallMessageComment.getUser();
        Society society = wallMessageComment.getWallMessage().getWall().getSociety();
        if (society.getOwner().getId().equals(userId) || user.getId().equals(userId)) {
            wallMessageRepository.deleteById(wallMessageCommentId);
        }
    }

    private boolean userCanAddMessageComment(User user, Wall wall) {
        return wall.getSociety() == null
                || societyService.isUserMemberOfSociety(user.getId(), wall.getSociety().getId());
    }

    private boolean userCanEditMessage(User user, Wall wall, WallMessageComment wallMessageComment) {
        return userCanAddMessageComment(user, wall) && wallMessageComment.getUser().getId().equals(user.getId());
    }

    private WallMessageComment addNewWallMessageComment(WallMessage wallMessage, User user, Wall wall, String message)
            throws ServiceException {
        if (userCanAddMessageComment(user, wall)) {
            WallMessageComment wallMessageComment = new WallMessageComment()
                    .setWallMessage(wallMessage)
                    .setUser(user);
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

    @Override
    public Optional<WallMessageCommentDto> addNewWallMessageComment(NewWallMessageCommentDto dto)
            throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = getWallMessageById(dto.getWallMessageId());
        User user = getUserById(dto.getUserId());
        Wall wall = wallMessage.getWall();
        return Optional.ofNullable(
                wallMessageCommentDtoMapper.fromEntity(
                        addNewWallMessageComment(wallMessage, user, wall, dto.getMessage())));
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

    @Override
    public Optional<WallMessageCommentDto> editWallMessageComment(EditMessageDto dto)
            throws ObjectNotFoundException, ServiceException {
        WallMessageComment wallMessageComment = findEntityById(dto.getMessageId());
        User user = getUserById(dto.getUserId());
        return Optional.ofNullable(wallMessageCommentDtoMapper.fromEntity(
                editWallMessageComment(user, wallMessageComment, dto.getMessage())));
    }

    @Override
    public void addLikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException {
        validateEntityNotNull(
                userRepository.findById(userId).orElse(null),
                USER_NOT_DEFINED_FOR_ID + userId);
        WallMessageComment wallMessageComment = findEntityById(messageId);
        int likesCount = wallMessageComment.getLikesCount();
        wallMessageComment.setLikesCount(++likesCount);
        wallMessageCommentRepository.save(wallMessageComment);
    }

    @Override
    public void addDislikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException {
        validateEntityNotNull(
                userRepository.findById(userId).orElse(null),
                USER_NOT_DEFINED_FOR_ID + userId);
        WallMessageComment wallMessageComment = findEntityById(messageId);
        int dislikesCount = wallMessageComment.getDislikesCount();
        wallMessageComment.setLikesCount(++dislikesCount);
        wallMessageCommentRepository.save(wallMessageComment);
    }
}
