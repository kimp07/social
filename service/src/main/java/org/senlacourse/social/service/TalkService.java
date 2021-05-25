package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.TalkMemberDto;
import org.senlacourse.social.mapstruct.TalkDtoMapper;
import org.senlacourse.social.mapstruct.TalkMemberDtoMapper;
import org.senlacourse.social.repository.TalkMemberRepository;
import org.senlacourse.social.repository.TalkRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
public class TalkService extends AbstractService<Talk> implements ITalkService {

    private final TalkRepository talkRepository;
    private final TalkMemberRepository talkMemberRepository;
    private final IUserService userService;
    private final TalkDtoMapper talkDtoMapper;
    private final TalkMemberDtoMapper talkMemberDtoMapper;

    @Override
    public Talk findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                talkRepository
                        .findById(id)
                        .orElse(null));
    }

    private Optional<TalkMember> getTalkMemberByUserIdAndTalkId(Long userId, Long talkId) {
        return talkMemberRepository.findOneByTalkIdAndUserId(talkId, userId);
    }

    @Override
    public Page<TalkDto> findAll(Pageable pageable) {
        return talkDtoMapper.map(talkRepository.findAll(pageable));
    }

    @Override
    public Page<TalkDto> findAllByUserIds(Long[] userId, Pageable pageable) {
        return talkDtoMapper.map(talkRepository.findAllByTalkMemberId(userId, pageable));
    }

    @AuthorizedUser
    @Override
    public boolean isUserMemberOfTalk(Long userId, Long talkId) throws ServiceException {
        return getTalkMemberByUserIdAndTalkId(userId, talkId).isPresent();
    }

    private TalkMember addTalkMemberToTalk(Talk talk, User user) {
        TalkMember talkMember = new TalkMember()
                .setTalk(talk)
                .setUser(user);
        return talkMemberRepository.save(talkMember);
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public TalkDto addNewTalk(NewTalkDto dto) throws ObjectNotFoundException, ServiceException {
        User sender = userService.findEntityById(dto.getSenderId());
        User recipient = userService.findEntityById(dto.getRecipientId());

        Talk talk = new Talk().setOwner(sender);
        talk = talkRepository.save(talk);

        addTalkMemberToTalk(talk, sender);
        addTalkMemberToTalk(talk, recipient);

        return talkDtoMapper.fromEntity(talk);
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public TalkMemberDto addTalkMemberToTalk(Long userId, Long talkId)
            throws ObjectNotFoundException, ServiceException {
        User user = userService.findEntityById(userId);
        Talk talk = findEntityById(talkId);
        return talkMemberDtoMapper
                .fromEntity(
                        getTalkMemberByUserIdAndTalkId(userId, talkId)
                                .orElse(
                                        addTalkMemberToTalk(talk, user)));
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void removeTalkMemberFromTalk(Long userId, Long talkId) throws ServiceException {
        getTalkMemberByUserIdAndTalkId(userId, talkId)
                .ifPresent(
                        talkMember -> talkMemberRepository.deleteById(talkMember.getId()));
    }
}
