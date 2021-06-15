package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.Talk;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITalkService extends IService<Talk> {

    Page<TalkDto> findAll(Pageable pageable);

    Page<TalkDto> findAllByUserId(Long userId, Pageable pageable);

    boolean isUserMemberOfTalk(Long userId, Long talkId) throws ServiceException;

    TalkDto addNewTalk(NewTalkDto dto) throws ObjectNotFoundException, ServiceException;

    void addTalkMemberToTalk(UserIdDto dto, Long talkId, Long memberId) throws ObjectNotFoundException, ServiceException;

    void removeTalkMemberFromTalk(UserIdDto dto, Long talkId) throws ServiceException;
}
