package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.TalkMessagesCache;
import org.senlacourse.social.domain.User;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Override
    TalkMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(talkMessageRepository.findById(id).orElse(null),
                "Talk message not defined for id=" + id);
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
                }
        );
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

    @Override
    public Optional<TalkMessageDto> addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException {
        if (talkService.isUserMemberOfTalk(dto.getUserId(), dto.getTalkId())) {
            User user = validateEntityNotNull(userRepository.findById(dto.getUserId()).orElse(null),
                    "User not defined for id=" + dto.getUserId());
            Talk talk = validateEntityNotNull(talkRepository.findById(dto.getTalkId()).orElse(null),
                    "Talk not defined for id=" + dto.getTalkId());
            return Optional.ofNullable(
                    talkMessageDtoMapper.fromEntity(
                            addNewMessage(user, talk, dto.getMessage())));
        } else {
            ServiceException e = new ServiceException("User with id=" + dto.getUserId() +
                    "can't add message to talk with id=" + dto.getTalkId());
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
