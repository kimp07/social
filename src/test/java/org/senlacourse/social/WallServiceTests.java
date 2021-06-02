package org.senlacourse.social;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.api.service.IWallService;
import org.senlacourse.social.dto.NewSocietyDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.SocietyDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.WallDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WallServiceTests {

    @Autowired
    ISocietyService societyService;
    @Autowired
    IWallService wallService;
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
    void canAddMessageToRootWall() {
        WallDto wallDto = wallService.findRootWall();
        Assertions.assertDoesNotThrow(() ->
                wallMessageService.addNewMessage(
                        new NewWallMessageDto()
                                .setWallId(wallDto.getId())
                                .setUserId(societyMember.getId())
                                .setMessage("New message to wall")));

    }

    @Test
    void notSocietyMemberCantAddMessageToSocietyWall() {
        SocietyDto societyDto = societyService.createNewSociety(
                new NewSocietyDto()
                        .setTitle("New society")
                        .setOwnerId(owner.getId()));
        WallDto wallDto = wallService.findWallBySocietyId(societyDto.getId());
        Assertions.assertThrows(Exception.class,
                () -> wallMessageService.addNewMessage(
                        new NewWallMessageDto()
                                .setWallId(wallDto.getId())
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
        WallDto wallDto = wallService.findWallBySocietyId(societyDto.getId());
        Assertions.assertDoesNotThrow(() -> wallMessageService.addNewMessage(
                        new NewWallMessageDto()
                                .setWallId(wallDto.getId())
                                .setUserId(societyMember.getId())
                                .setMessage("New message to wall")));
    }
}
