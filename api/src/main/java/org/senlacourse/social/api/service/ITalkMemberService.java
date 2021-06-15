package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.dto.TalkMemberDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITalkMemberService {
    boolean isUserTalkMember(Long userId, Long talkId);

    TalkMember findEntityByUserIdAndTalkId(Long userId, Long talkId) throws ObjectNotFoundException;

    TalkMemberDto findByUserIdAndTalkId(Long userId, Long talkId) throws ObjectNotFoundException;

    Page<TalkMemberDto> findAllByTalkId(Long talkId, Pageable pageable);

    void save(UserIdDto dto, Long talkId) throws ObjectNotFoundException, ServiceException;
}
