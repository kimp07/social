package org.senlacourse.social;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.repository.TalkRepository;
import org.senlacourse.social.service.ICorrespondenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TalkMessageTests {

    @Autowired
    ITalkMessageService talkMessageService;
    @Autowired
    ITalkService talkService;
    @Autowired
    ICorrespondenceService correspondenceService;
    @Autowired
    IUserService userService;
    @Autowired
    TalkRepository talkRepository;

    UserDto sender;
    UserDto recipient;
    TalkDto talkDto;

    @BeforeEach
    void setUp() {
        sender = userService.saveUser(
                new NewUserDto()
                        .setLogin("talk_member1")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("talk_member1@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
        recipient = userService.saveUser(
                new NewUserDto()
                        .setLogin("talk_member2")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("talk_member2@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
        talkDto = talkService.addNewTalk(
                new NewTalkDto()
                        .setSenderId(sender.getId())
                        .setRecipientId(recipient.getId()));
    }

    @AfterEach
    void afterTest() {
        if (sender != null) {
            userService.deleteById(new UserIdDto(sender.getId()));
        }
        if (recipient != null) {
            userService.deleteById(new UserIdDto(recipient.getId()));
        }
        if (talkDto != null) {
            talkRepository.deleteById(talkDto.getId());
        }
    }

    @Test
    void mustCreatedUnreadTalkMessages() {
        talkMessageService.addNewMessage(
                new NewTalkMessageDto()
                        .setTalkId(talkDto.getId())
                        .setUserId(sender.getId())
                        .setMessage("Some message"));
        Assertions.assertNotNull(
                correspondenceService
                        .getCountUnreadMessagesByUserIdGroupByTalkId(
                                new UserIdDto(recipient.getId()),
                                        talkDto.getId(),
                                        PageRequest.of(0, 1))
                        .getContent());
    }
}
