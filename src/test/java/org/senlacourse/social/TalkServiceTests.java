package org.senlacourse.social;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.repository.TalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TalkServiceTests {

    @Autowired
    IUserService userService;
    @Autowired
    ITalkService talkService;
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
    void addNewTalkTestMustCreateNewTalk() {
        talkDto = talkService.addNewTalk(
                new NewTalkDto()
                        .setSenderId(sender.getId())
                        .setRecipientId(recipient.getId()));
        Assertions.assertNotNull(talkDto);
    }

    @Test
    void mustAddNewMemberToTalk() {
        UserDto thirdMember = userService.saveUser(
                new NewUserDto()
                        .setLogin("talk_member3")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("talk_member3@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
        talkDto = talkService.addNewTalk(
                new NewTalkDto()
                        .setSenderId(sender.getId())
                        .setRecipientId(recipient.getId()));
        talkService.addTalkMemberToTalk(
                new UserIdDto(sender.getId()),
                talkDto.getId(),
                thirdMember.getId());
        Assertions.assertTrue(
                talkService
                        .isUserMemberOfTalk(thirdMember.getId(), talkDto.getId()));
    }
}
