package org.senlacourse.social;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FriendshipRequestServiceTests {

    @Autowired
    IFriendshipRequestService friendshipRequestService;
    @Autowired
    IFriendshipService friendshipService;
    @Autowired
    IUserService userService;
    UserDto sender;
    UserDto recipient;
    FriendshipRequestDto requestDto;

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

    @AfterEach
    void afterTest() {
        userService.deleteById(new UserIdDto(sender.getId()));
        userService.deleteById(new UserIdDto(recipient.getId()));
    }

    private FriendshipRequestDto saveNewRequest() {
        return friendshipRequestService.saveNewFriendshipRequest(
                new NewFriendshipRequestDto()
                        .setSenderId(sender.getId())
                        .setRecipientId(recipient.getId()));
    }

    @Test
    void sendFriendshipRequestMustExistsInDatabaseAfterSaving() {
        requestDto = saveNewRequest();
        Assertions.assertNotNull(
                friendshipRequestService
                        .findEntityById(
                                requestDto.getId()));
    }

    @Test
    void confirmFriendshipRequestMustBeCreatedFriendship() {
        requestDto = saveNewRequest();
        friendshipRequestService.confirmFriendshipRequestById(
                new UserIdDto(recipient.getId()), requestDto.getId());
        Assertions.assertTrue(
                friendshipService
                        .friendshipExistsByBothUserIds(
                                requestDto.getSender().getId(),
                                requestDto.getRecipient().getId()));
    }
}
