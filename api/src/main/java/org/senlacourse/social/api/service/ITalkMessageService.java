package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;

import java.util.Optional;

public interface ITalkMessageService {
    Optional<TalkMessageDto> addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException;
}
