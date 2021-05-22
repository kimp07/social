package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.TalkMessagesCache;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.projection.ITalkMessagesCacheTalksCountView;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.senlacourse.social.mapstruct.TalkMessageDtoMapper;
import org.senlacourse.social.repository.TalkMemberRepository;
import org.senlacourse.social.repository.TalkMessageRepository;
import org.senlacourse.social.repository.TalkMessagesCacheRepository;
import org.senlacourse.social.repository.TalkRepository;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class TalkMessageService extends AbstractService<TalkMessage> implements ITalkMessageService {

    private final TalkMessageRepository talkMessageRepository;
    private final UserRepository userRepository;
    private final TalkRepository talkRepository;
    private final TalkMemberRepository talkMemberRepository;
    private final TalkMessagesCacheRepository talkMessagesCacheRepository;
    private final TalkMessageDtoMapper talkMessageDtoMapper;
    private final ITalkService talkService;
    private final IAuthorizedUserService authorizedUserService;

    @Override
    TalkMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(talkMessageRepository.findById(id).orElse(null),
                "Talk message not defined for id=" + id);
    }

    private User findUserById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(userRepository.findById(id).orElse(null),
                "User not defined for id=" + id);
    }

    private Talk findTalkById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(talkRepository.findById(id).orElse(null),
                "Talk not defined for id=" + id);
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
                talkMessageRepository.findAllByTalkId(talkId, pageable).getTotalPages(),
                pageable.getPageSize());
    }

    @Override
    public Page<TalkMessageDto> findAllByTalkId(Long talkId, Pageable pageable) throws ObjectNotFoundException {
        findTalkById(talkId);
        return talkMessageDtoMapper.map(
                talkMessageRepository.findAllByTalkId(
                        talkId,
                        getLastPageOfTalkMessages(talkId, pageable)));
    }

    @Override
    public TalkMessageDto addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException {
        authorizedUserService.injectAuthorizedUserId(dto);
        if (talkService.isUserMemberOfTalk(dto.getUserId(), dto.getTalkId())) {
            User user = findUserById(dto.getUserId());
            Talk talk = findTalkById(dto.getTalkId());
            return talkMessageDtoMapper.fromEntity(
                    addNewMessage(user, talk, dto.getMessage()));
        } else {
            ServiceException e = new ServiceException("User with id=" + dto.getUserId() +
                    "can't add message to talk with id=" + dto.getTalkId());
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ITalkMessagesCacheTalksCountView> findCacheMessagesByRecipientIdGroupByTalkId(Long recipientId,
                                                                                              Pageable pageable)
            throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(recipientId);
        findUserById(recipientId);
        return talkMessagesCacheRepository.findAllByRecipientIdGroupByTalkId(recipientId, pageable);
    }

    @Override
    public ITalkMessagesCacheTalksCountView findCacheMessagesCountByRecipientIdAndTalkId(Long recipientId,
                                                                                         Long talkId)
            throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(recipientId);
        findUserById(recipientId);
        findTalkById(talkId);
        return talkMessagesCacheRepository.getCountByRecipientIdAndTalkId(recipientId, talkId);
    }

}
