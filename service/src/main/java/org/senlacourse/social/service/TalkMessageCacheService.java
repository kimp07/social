package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class TalkMessageCacheService {

    private final UserRepository userRepository;

    public void addMessagesToCache(Long talkId, Long talkMessageId, Long senderId) {

    }
}
