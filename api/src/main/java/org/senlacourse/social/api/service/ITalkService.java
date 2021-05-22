package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.TalkMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITalkService {

    Page<TalkDto> findAll(Pageable pageable);

    Page<TalkDto> findAllByUserIds(Long[] userId, Pageable pageable);

    boolean isUserMemberOfTalk(Long userId, Long talkId) throws ServiceException;

    TalkDto addNewTalk(NewTalkDto dto) throws ObjectNotFoundException, ServiceException;

    TalkMemberDto addTalkMemberToTalk(Long talkId, Long userId)
            throws ObjectNotFoundException, ServiceException;

    void removeTalkMemberFromTalk(Long talkId, Long userId) throws ServiceException;
}
