package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.*;
import org.senlacourse.social.domain.projection.ITalkMessagesCacheTalksCountView;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.TalkMessageDtoMapper;
import org.senlacourse.social.repository.TalkMemberRepository;
import org.senlacourse.social.repository.TalkMessageRepository;
import org.senlacourse.social.repository.TalkMessagesCacheRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Log4j
public class TalkMessageService extends AbstractService<TalkMessage> implements ITalkMessageService {

    private final TalkMessageRepository talkMessageRepository;
    private final IUserService userService;
    private final TalkMemberRepository talkMemberRepository;
    private final TalkMessagesCacheRepository talkMessagesCacheRepository;
    private final TalkMessageDtoMapper talkMessageDtoMapper;
    private final ITalkService talkService;

    @Override
    public TalkMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                talkMessageRepository
                        .findById(id)
                        .orElse(null));
    }

    private void sendMessageToCacheForTalkMembers(TalkMessage talkMessage,
                                                  Page<TalkMember> talkMembersPage,
                                                  User sender) {
        talkMembersPage.forEach(talkMember -> {
            if (!talkMember.getUser().getId().equals(sender.getId())) {
                talkMessagesCacheRepository.save(new TalkMessagesCache()
                        .setTalkMessage(talkMessage)
                        .setRecipient(talkMember.getUser()));
            }
        });
    }

    private void sendMessagesToCache(Talk talk, TalkMessage talkMessage, User sender) {
        int pageSize = 20;
        Page<TalkMember> talkMembersPage
                = talkMemberRepository.findAllByTalkId(talk.getId(), PageRequest.of(0, pageSize));
        int totalPages = talkMembersPage.getTotalPages();
        sendMessageToCacheForTalkMembers(talkMessage, talkMembersPage, sender);
        if (talkMembersPage.getTotalPages() > 1) {
            for (int pageNum = 1; pageNum < totalPages; pageNum++) {
                talkMembersPage
                        = talkMemberRepository.findAllByTalkId(talk.getId(), PageRequest.of(pageNum, pageSize));
                sendMessageToCacheForTalkMembers(talkMessage, talkMembersPage, sender);
            }
        }
    }

    private TalkMessage addNewMessage(User user, Talk talk, String message) {
        TalkMessage talkMessage = talkMessageRepository
                .save(new TalkMessage()
                        .setMessage(message)
                        .setTalk(talk)
                        .setUser(user)
                        .setMessageDate(LocalDateTime.now()));
        sendMessagesToCache(talk, talkMessage, user);
        return talkMessage;
    }

    private Pageable getLastPageOfTalkMessages(Long talkId, Pageable pageable) {
        return PageRequest.of(
                talkMessageRepository
                        .findAllByTalkId(
                                talkId,
                                PageRequest.of(
                                        0,
                                        pageable.getPageSize(),
                                        pageable.getSort()))
                        .getTotalPages(),
                pageable.getPageSize(),
                pageable.getSort());
    }

    @Override
    public Page<TalkMessageDto> findAllByTalkId(Long talkId, Pageable pageable) throws ObjectNotFoundException {
        talkService.findEntityById(talkId);
        return talkMessageDtoMapper.map(
                talkMessageRepository.findAllByTalkId(
                        talkId,
                        pageable.getPageNumber() == Integer.MAX_VALUE ? getLastPageOfTalkMessages(talkId, pageable) : pageable));
    }

    @AuthorizedUser
    @Override
    public TalkMessageDto addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException {
        if (talkService.isUserMemberOfTalk(dto.getUserId(), dto.getTalkId())) {
            User user = userService.findEntityById(dto.getUserId());
            Talk talk = talkService.findEntityById(dto.getTalkId());
            return talkMessageDtoMapper.fromEntity(
                    addNewMessage(user, talk, dto.getMessage()));
        } else {
            ServiceException e = new ServiceException("User with id=" + dto.getUserId() +
                    "can't add message to talk with id=" + dto.getTalkId());
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public Page<ITalkMessagesCacheTalksCountView> findCacheMessagesByRecipientIdGroupByTalkId(UserIdDto dto,
                                                                                              Pageable pageable)
            throws ObjectNotFoundException {
        return talkMessagesCacheRepository.findAllByRecipientIdGroupByTalkId(dto.getAuthorizedUserId(), pageable);
    }

    @AuthorizedUser
    @Override
    public ITalkMessagesCacheTalksCountView findCacheMessagesCountByRecipientIdAndTalkId(UserIdDto dto,
                                                                                         Long talkId)
            throws ObjectNotFoundException {
        return talkMessagesCacheRepository.getCountByRecipientIdAndTalkId(dto.getAuthorizedUserId(), talkId);
    }

    @AuthorizedUser
    @Override
    public void deleteCacheMessagesByRecipientId(UserIdDto dto) {
        talkMessagesCacheRepository.deleteAllByRecipientId(dto.getAuthorizedUserId());
    }

    @AuthorizedUser
    @Override
    public void deleteCacheMessagesByRecipientIdAndTalkId(UserIdDto dto, Long talkId) {
        talkMessagesCacheRepository.deleteAllByRecipientIdAndTalkId(dto.getAuthorizedUserId(), talkId);
    }

}
