package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.domain.Society;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.WallMessageDto;
import org.senlacourse.social.mapstruct.WallMessageDtoMapper;
import org.senlacourse.social.repository.SocietyRepository;
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
public class WallMessageService extends AbstractService<WallMessage> implements IWallMessageService {

    private static final String USER_WITH_ID = "User with id=";

    private final WallMessageRepository wallMessageRepository;
    private final WallMessageCommentRepository wallMessageCommentRepository;
    private final IUserService userService;
    private final ISocietyService societyService;
    private final WallMessageDtoMapper wallMessageDtoMapper;
    private final SocietyRepository societyRepository;

    private Society findSocietyById(Long id) throws ObjectNotFoundException {
        return societyRepository.findById(id)
                .<ObjectNotFoundException>orElseThrow(() -> {
                    ObjectNotFoundException e = new ObjectNotFoundException();
                    log.error(e.getMessage(), e);
                    throw e;
                });
    }


    @Override
    public WallMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                wallMessageRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public Page<WallMessageDto> findAllBySocietyId(Long societyId, Pageable pageable) {
        return wallMessageDtoMapper.map(
                wallMessageRepository
                        .findAllBySocietyId(
                                societyId,
                                pageable));
    }

    @AuthorizedUser
    @Override
    public void deleteAllMessagesBySocietyIdAndUserId(UserIdDto dto, Long societyId)
            throws ObjectNotFoundException, ServiceException {
        Society society = findSocietyById(societyId);
        User owner = society.getOwner();
        if (owner.getId().equals(dto.getAuthorizedUserId())) {
            wallMessageRepository.deleteAllBySocietyId(societyId);
            wallMessageCommentRepository.deleteAllBySocietyId(societyId);
        }
    }

    @AuthorizedUser
    @Override
    public void deleteByMessageIdAndUserId(UserIdDto dto, Long wallMessageId)
            throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = findEntityById(wallMessageId);
        User user = wallMessage.getUser();
        Society society = wallMessage.getSociety();
        if (user.getId().equals(dto.getAuthorizedUserId()) || society.getOwner().getId().equals(dto.getAuthorizedUserId())) {
            wallMessageRepository.deleteById(wallMessageId);
            wallMessageCommentRepository.deleteAllByWallMessageId(wallMessageId);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + dto.getAuthorizedUserId() +
                            " can`t delete message with id=" + wallMessageId);
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    private boolean userCanAddMessage(User user, Society society) {
        return society.getRoot()
                || societyService.isUserMemberOfSociety(user.getId(), society.getId());
    }

    private boolean userCanEditMessage(User user, Society society, WallMessage wallMessage) {
        return userCanAddMessage(user, society) && wallMessage.getUser().getId().equals(user.getId());
    }

    private WallMessage addNewMessage(User user, Society society, String message) throws ServiceException {
        if (userCanAddMessage(user, society)) {
            WallMessage wallMessage = new WallMessage();
            wallMessage.setSociety(society)
                    .setUser(user)
                    .setMessage(message)
                    .setMessageDate(LocalDateTime.now())
                    .setLikesCount(0)
                    .setDislikesCount(0);
            return wallMessageRepository.save(wallMessage);
        } else {
            ServiceException e = new ServiceException(
                    USER_WITH_ID + user.getId() +
                            " can`t add message to society wall with id=" + society.getId());
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public WallMessageDto addNewMessage(NewWallMessageDto dto) throws ObjectNotFoundException, ServiceException {
        User user = userService.findEntityById(dto.getUserId());
        Society society = findSocietyById(dto.getSocietyId());
        return wallMessageDtoMapper
                .fromEntity(
                        addNewMessage(user, society, dto.getMessage()));
    }

    private WallMessage editWallMessage(User user, WallMessage wallMessage, String message)
            throws ServiceException {
        Society wall = wallMessage.getSociety();
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
    @Override
    public WallMessageDto editWallMessage(EditMessageDto dto) throws ObjectNotFoundException, ServiceException {
        User user = userService.findEntityById(dto.getUserId());
        WallMessage wallMessage = findEntityById(dto.getMessageId());
        return wallMessageDtoMapper.fromEntity(
                editWallMessage(user, wallMessage, dto.getMessage()));
    }

    @Override
    public void addLikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = findEntityById(messageId);
        int likesCount = wallMessage.getLikesCount();
        wallMessage.setLikesCount(++likesCount);
        wallMessageRepository.save(wallMessage);
    }

    @Override
    public void addDislikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException {
        WallMessage wallMessage = findEntityById(messageId);
        int dislikesCount = wallMessage.getDislikesCount();
        wallMessage.setLikesCount(++dislikesCount);
        wallMessageRepository.save(wallMessage);
    }
}
