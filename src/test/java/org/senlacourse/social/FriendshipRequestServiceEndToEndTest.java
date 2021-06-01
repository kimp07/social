package org.senlacourse.social;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FriendshipRequestServiceEndToEndTest {

    @Autowired
    IFriendshipRequestService friendshipRequestService;
    @Autowired
    IUserService userService;
    UserDto sender;
    UserDto recipient;

    @BeforeEach
    void setUp() {
        sender = userService.saveUser(
                new NewUserDto()
                        .setLogin("sender")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("sender@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
        recipient = userService.saveUser(
                new NewUserDto()
                        .setLogin("recipient")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("recipient@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
    }

    @Test
    void sendFriendshipRequestMustExistsInDatabaseAfterSaving() {
        FriendshipRequestDto requestDto = friendshipRequestService.saveNewFriendshipRequest(
                new NewFriendshipRequestDto()
                        .setSenderId(sender.getId())
                        .setRecipientId(recipient.getId()));
        Assertions.assertNotNull(
                friendshipRequestService
                        .findEntityById(
                                requestDto.getId()));
    }
}
