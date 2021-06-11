package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.projection.IUnreadTalkMessagesGroupByTalkIdCountView;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.TalkMessageDtoMapper;
import org.senlacourse.social.repository.TalkMemberRepository;
import org.senlacourse.social.repository.TalkMessageRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
@Log4j
public class TalkMessageService extends AbstractService<TalkMessage> implements ITalkMessageService {

    private final TalkMessageRepository talkMessageRepository;
    private final IUserService userService;
    private final TalkMemberRepository talkMemberRepository;
    private final TalkMessageDtoMapper talkMessageDtoMapper;
    private final ITalkService talkService;

    @Override
    public TalkMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                talkMessageRepository
                        .findById(id)
                        .orElse(null));
    }

    private void sendMessageToTalkMembers(String message,
                                          Page<TalkMember> talkMembersPage,
                                          User sender,
                                          TalkMessage answeredMessage,
                                          LocalDateTime dateTime) {
        talkMembersPage.forEach(talkMember ->
                talkMessageRepository.save(new TalkMessage()
                        .setMessageDate(dateTime)
                        .setSender(sender)
                        .setMessage(message)
                        .setTalk(talkMember.getId().getTalk())
                        .setUser(talkMember.getId().getUser())
                        .setAnsweredMessage(answeredMessage)));
    }

    private void sendMessagesToTalkMembers(Talk talk, String message, User sender, TalkMessage answeredMessage) {
        int pageSize = 20;
        LocalDateTime now = LocalDateTime.now();
        Page<TalkMember> talkMembersPage
                = talkMemberRepository.findAllByIdTalkId(talk.getId(), PageRequest.of(0, pageSize));
        int totalPages = talkMembersPage.getTotalPages();
        sendMessageToTalkMembers(message, talkMembersPage, sender, answeredMessage, now);
        if (talkMembersPage.getTotalPages() > 1) {
            for (int pageNum = 1; pageNum < totalPages; pageNum++) {
                talkMembersPage
                        = talkMemberRepository.findAllByIdTalkId(talk.getId(), PageRequest.of(pageNum, pageSize));
                sendMessageToTalkMembers(message, talkMembersPage, sender, answeredMessage, now);
            }
        }
    }

    private void addNewMessage(User user, Talk talk, String message, TalkMessage answeredMessage) {
        sendMessagesToTalkMembers(talk, message, user, answeredMessage);
    }

    @Override
    public Page<TalkMessageDto> findAllByTalkId(Long talkId, Pageable pageable) throws ObjectNotFoundException {
        talkService.findEntityById(talkId);
        return talkMessageDtoMapper.map(
                talkMessageRepository.findAllByTalkId(
                        talkId,
                        pageable));
    }

    @AuthorizedUser
    @Override
    public void addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException {
        if (talkService.isUserMemberOfTalk(dto.getUserId(), dto.getTalkId())) {
            addNewMessage(
                    userService.findEntityById(dto.getUserId()),
                    talkService.findEntityById(dto.getTalkId()),
                    dto.getMessage(),
                    findEntityById(dto.getAnsweredMessage()));
        } else {
            ServiceException e = new ServiceException("User with id=" + dto.getUserId() +
                    "can't add message to talk with id=" + dto.getTalkId());
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public Page<IUnreadTalkMessagesGroupByTalkIdCountView> getUnreadMessagesByRecipientIdGroupByTalkId(UserIdDto dto,
                                                                                                       Pageable pageable)
            throws ObjectNotFoundException {
        return talkMessageRepository.findCountByUserIdAndUnreadIsTrueGroupByTalkId(dto.getAuthorizedUserId(), pageable);
    }

    @AuthorizedUser
    @Override
    public void updateMessagesSetUnreadFalseByRecipientId(UserIdDto dto) {
        talkMessageRepository.updateAllSetUnreadFalseByUserId(dto.getAuthorizedUserId());
    }

    @AuthorizedUser
    @Override
    public void updateMessagesSetUnreadFalseByRecipientIdAndTalkId(UserIdDto dto, Long talkId) {
        talkMessageRepository.updateAllSetUnreadFalseByUserAndTalkId(dto.getAuthorizedUserId(), talkId);
    }

}
