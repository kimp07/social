package org.senlacourse.social;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WallMessageServiceTests {

    @Autowired
    ISocietyService societyService;
    @Autowired
    IWallMessageService wallMessageService;
    @Autowired
    IUserService userService;

    UserDto owner;
    UserDto societyMember;

    @BeforeEach
    void setUp() {
        owner = userService.saveUser(
                new NewUserDto()
                        .setLogin("society_owner")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("society_owner@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
        societyMember = userService.saveUser(
                new NewUserDto()
                        .setLogin("society_member")
                        .setFirstName("FirstName")
                        .setSurname("Surname")
                        .setEmail("society_member@mail.com")
                        .setPassword("password")
                        .setBirthDate("1990-02-15")
                        .setAboutMe("")
                        .setRoleId(1L));
    }

    @AfterEach
    void afterTest() {
        if (owner != null) {
            userService.deleteById(new UserIdDto(owner.getId()));
        }
        if (societyMember != null) {
            userService.deleteById(new UserIdDto(societyMember.getId()));
        }
    }

    @Test
    void canAddMessageToRootSociety() {
        SocietyDto societyDto = societyService.findRootSociety();
        Assertions.assertDoesNotThrow(() ->
                wallMessageService.addNewMessage(
                        new NewWallMessageDto()
                                .setSocietyId(societyDto.getId())
                                .setUserId(societyMember.getId())
                                .setMessage("New message to wall")));

    }

    @Test
    void notSocietyMemberCantAddMessageToSocietyWall() {
        SocietyDto societyDto = societyService.createNewSociety(
                new NewSocietyDto()
                        .setTitle("New society")
                        .setOwnerId(owner.getId()));
        Assertions.assertThrows(Exception.class,
                () -> wallMessageService.addNewMessage(
                        new NewWallMessageDto()
                                .setSocietyId(societyDto.getId())
                                .setUserId(societyMember.getId())
                                .setMessage("New message to wall")));
    }

    @Test
    void societyMemberCanAddMessageToSocietyWall() {
        SocietyDto societyDto = societyService.createNewSociety(
                new NewSocietyDto()
                        .setTitle("New other society")
                        .setOwnerId(owner.getId()));
        societyService.addUserToSociety(
                new UserIdDto(societyMember.getId()),
                societyDto.getId());
        Assertions.assertDoesNotThrow(() -> wallMessageService.addNewMessage(
                        new NewWallMessageDto()
                                .setSocietyId(societyDto.getId())
                                .setUserId(societyMember.getId())
                                .setMessage("New message to wall")));
    }
}
