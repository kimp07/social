package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.TalkMemberDto;

import java.util.Optional;

public interface ITalkService {

    boolean isUserMemberOfTalk(Long userId, Long talkId) throws ServiceException;

    Optional<TalkDto> addNewTalk(NewTalkDto dto) throws ObjectNotFoundException, ServiceException;

    Optional<TalkMemberDto> addTalkMemberToTalk(Long talkId, Long userId)
            throws ObjectNotFoundException, ServiceException;

    void removeTalkMemberFromTalk(Long talkId, Long userId) throws ServiceException;
}
