package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.TalkMemberId;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.TalkMemberDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.TalkMemberDtoMapper;
import org.senlacourse.social.repository.TalkMemberRepository;
import org.senlacourse.social.repository.TalkRepository;
import org.senlacourse.social.repository.UserRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
public class TalkMemberService {

    private final TalkMemberRepository talkMemberRepository;
    private final UserRepository userRepository;
    private final TalkRepository talkRepository;
    private final TalkMemberDtoMapper talkMemberDtoMapper;

    private Optional<TalkMember> getByUserIdAndTalkId(Long userId, Long talkId) {
        return talkMemberRepository
                .findOneByIdTalkIdAndIdUserId(talkId, userId);
    }

    private User findUserById(Long id) throws ObjectNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    ObjectNotFoundException e = new ObjectNotFoundException();
                    log.error(e.getMessage(), e);
                    throw e;
                });
    }

    private Talk findTalkById(Long id) throws ObjectNotFoundException {
        return talkRepository.findById(id)
                .orElseThrow(() -> {
                    ObjectNotFoundException e = new ObjectNotFoundException();
                    log.error(e.getMessage(), e);
                    throw e;
                });
    }

    public boolean isUserTalkMember(Long userId, Long talkId) {
        return getByUserIdAndTalkId(userId, talkId).isPresent();
    }

    public TalkMember findEntityByUserIdAndTalkId(Long userId, Long talkId) throws ObjectNotFoundException {
        return getByUserIdAndTalkId(userId, talkId)
                .orElseThrow(() -> {
                    ObjectNotFoundException e = new ObjectNotFoundException();
                    log.error(e.getMessage(), e);
                    throw e;
                });
    }

    public TalkMemberDto findByUserIdAndTalkId(Long userId, Long talkId) throws ObjectNotFoundException {
        return talkMemberDtoMapper.fromEntity(
                findEntityByUserIdAndTalkId(userId, talkId));
    }

    public Page<TalkMemberDto> findAllByTalkId(Long talkId, Pageable pageable) {
        return talkMemberDtoMapper.map(
                talkMemberRepository.findAllByIdTalkId(talkId, pageable));
    }

    private void saveTalkMember(User user, Talk talk) {
        talkMemberRepository.save(
                new TalkMember().setId(
                        new TalkMemberId()
                        .setTalk(talk)
                        .setUser(user)));
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    public void save(UserIdDto dto, Long talkId) throws ObjectNotFoundException, ServiceException {
        User user = findUserById(dto.getAuthorizedUserId());
        Talk talk = findTalkById(talkId);
        if (isUserTalkMember(dto.getAuthorizedUserId(), talkId)) {
            ServiceException e = new ServiceException("User is member of talk");
            log.error(e.getMessage(), e);
            throw e;
        }
        saveTalkMember(user, talk);
    }

}
