package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Correspondence;
import org.senlacourse.social.domain.CorrespondenceId;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.senlacourse.social.mapstruct.TalkMessageDtoMapper;
import org.senlacourse.social.repository.CorrespondenceRepository;
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
    private final CorrespondenceRepository correspondenceRepository;
    private final TalkMessageDtoMapper talkMessageDtoMapper;
    private final ITalkService talkService;

    @Override
    public TalkMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                talkMessageRepository
                        .findByTalkMessageId(id)
                        .orElse(null));
    }

    private void sendMessageToTalkMembers(Page<TalkMember> talkMembersPage,
                                          TalkMessage talkMessage, User sender) {
        talkMembersPage.forEach(talkMember ->
            correspondenceRepository.save(
                    new Correspondence()
                            .setUnread(!talkMember.getId().getUser().equals(sender))
                            .setId(new CorrespondenceId()
                                    .setTalkMessage(talkMessage)
                                    .setUser(talkMember.getId().getUser())))
        );
    }

    private void sendMessagesToTalkMembers(Talk talk, TalkMessage talkMessage, User sender) {
        int pageSize = 20;
        Page<TalkMember> talkMembersPage
                = talkMemberRepository.findAllByIdTalkId(talk.getId(), PageRequest.of(0, pageSize));
        int totalPages = talkMembersPage.getTotalPages();
        sendMessageToTalkMembers(talkMembersPage, talkMessage, sender);
        if (talkMembersPage.getTotalPages() > 1) {
            for (int pageNum = 1; pageNum < totalPages; pageNum++) {
                talkMembersPage
                        = talkMemberRepository.findAllByIdTalkId(talk.getId(), PageRequest.of(pageNum, pageSize));
                sendMessageToTalkMembers(talkMembersPage, talkMessage, sender);
            }
        }
    }

    private void addNewMessage(User sender, Talk talk, String message, TalkMessage answeredMessage) {
        TalkMessage talkMessage = talkMessageRepository.save(new TalkMessage()
                .setMessageDate(LocalDateTime.now())
                .setSender(sender)
                .setMessage(message)
                .setTalk(talk)
                .setAnsweredMessage(answeredMessage));
        sendMessagesToTalkMembers(talk, talkMessage, sender);
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
    public void addNewMessage(NewTalkMessageDto dto) throws ObjectNotFoundException, ServiceException {
        if (!talkService.isUserMemberOfTalk(dto.getUserId(), dto.getTalkId())) {
            ServiceException e = new ServiceException("User with id=" + dto.getUserId() +
                    "can't add message to talk with id=" + dto.getTalkId());
            log.error(e.getMessage(), e);
            throw e;
        }
        TalkMessage answeredMessage = null;
        if (dto.getAnsweredMessageId() != null && !dto.getAnsweredMessageId().equals(0L)) {
            answeredMessage = findEntityById(dto.getAnsweredMessageId());
        }
        addNewMessage(
                userService.findEntityById(dto.getUserId()),
                talkService.findEntityById(dto.getTalkId()),
                dto.getMessage(),
                answeredMessage);
    }
}
