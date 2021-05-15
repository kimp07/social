package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.TalkMemberDto;

import java.util.Optional;

public interface ITalkService {
    boolean isUserMemberOfTalk(Long userId, Long talkId);

    Optional<TalkDto> addNewTalk(NewTalkDto dto) throws ObjectNotFoundException;

    Optional<TalkMemberDto> addTalkMemberToTalk(Long talkId, Long userId) throws ObjectNotFoundException;

    void removeTalkMemberFromTalk(Long talkId, Long userId);
}
