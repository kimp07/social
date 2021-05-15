package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.senlacourse.social.mapstruct.TalkMessageDtoMapper;
import org.senlacourse.social.repository.TalkMemberRepository;
import org.senlacourse.social.repository.TalkMessageRepository;
import org.senlacourse.social.repository.TalkRepository;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class TalkMessageService extends AbstractService<TalkMessage> {

    private final TalkMessageRepository talkMessageRepository;
    private final UserRepository userRepository;
    private final TalkRepository talkRepository;
    private final TalkMemberRepository talkMemberRepository;
    private final TalkMessageDtoMapper talkMessageDtoMapper;

    @Override
    TalkMessage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(talkMessageRepository.findById(id).orElse(null),
                "Talk message not defined for id=" + id);
    }

    private void sendMessagesToCache() {

    }

    public Optional<TalkMessageDto> addNewMessage(NewTalkMessageDto dto) throws ObjectNotFoundException {
        User user = validateEntityNotNull(userRepository.findById(dto.getUserId()).orElse(null),
                "User not defined for id=" + dto.getUserId());
        // TODO
        return null;
    }
}
