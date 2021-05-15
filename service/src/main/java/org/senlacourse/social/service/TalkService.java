package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
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
import org.senlacourse.social.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class TalkService extends AbstractService<Talk> {

    private final TalkRepository talkRepository;
    private final TalkMemberRepository talkMemberRepository;
    private final UserRepository userRepository;
    private final TalkDtoMapper talkDtoMapper;
    private final TalkMemberDtoMapper talkMemberDtoMapper;

    @Override
    Talk findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(talkRepository.findById(id).orElse(null),
                "Talk not defined for id=" + id);
    }

    private User getUserById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(userRepository.findById(id).orElse(null),
                "User not defined for id=" + id);
    }

    private Talk getTalkById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(talkRepository.findById(id).orElse(null),
                "Talk not defined for id=" + id);
    }

    private Optional<TalkMember> getTalkMemberByUserIdAndTalkId(Long userId, Long talkId) {
        return talkMemberRepository.findOneByTalkIdAndUserId(talkId, userId);
    }

    public boolean isUserMemberOfTalk(Long userId, Long talkId) {
        return getTalkMemberByUserIdAndTalkId(userId, talkId).isPresent();
    }

    private TalkMember addTalkMemberToTalk(Talk talk, User user) {
        TalkMember talkMember = new TalkMember()
                .setTalk(talk)
                .setUser(user);
        return talkMemberRepository.save(talkMember);
    }

    public Optional<TalkDto> addNewTalk(NewTalkDto dto) throws ObjectNotFoundException {
        User sender = getUserById(dto.getSenderId());
        User recipient = getUserById(dto.getRecipientId());

        Talk talk = new Talk().setOwner(sender);
        talk = talkRepository.save(talk);

        addTalkMemberToTalk(talk, sender);
        addTalkMemberToTalk(talk, recipient);

        return Optional.ofNullable(
                talkDtoMapper.fromEntity(talk));
    }

    public Optional<TalkMemberDto> addTalkMemberToTalk(Long talkId, Long userId) throws ObjectNotFoundException {
        User user = getUserById(userId);
        Talk talk = getTalkById(talkId);
        return Optional.ofNullable(
                talkMemberDtoMapper
                        .fromEntity(
                                getTalkMemberByUserIdAndTalkId(userId, talkId)
                                        .orElse(
                                                addTalkMemberToTalk(talk, user))));
    }

    public void removeTalkMemberFromTalk(Long talkId, Long userId) {
        getTalkMemberByUserIdAndTalkId(userId, talkId)
                .ifPresent(
                        talkMember -> talkMemberRepository.deleteById(talkMember.getId()));
    }
}
