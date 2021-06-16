package org.senlacourse.social;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.*;
import org.senlacourse.social.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SocietyServiceTests {

    @Autowired
    IUserService userService;
    @Autowired
    ISocietyService societyService;
    @Autowired
    SocietyRepository societyRepository;

    UserDto owner;
    UserDto societyMember;
    SocietyDto societyDto;

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
    void afterEach() {
        if (owner != null) {
            userService.deleteById(new UserIdDto(owner.getId()));
        }
        if (societyMember != null) {
            userService.deleteById(new UserIdDto(societyMember.getId()));
        }
        if (societyDto != null) {
            societyRepository.deleteById(societyDto.getId());
        }
    }

    @Test
    void successfullyAddingNewSocietyMember() {
        societyDto = societyService.createNewSociety(
                new NewSocietyDto()
                        .setOwnerId(owner.getId())
                        .setTitle("Society"));
        SocietyMemberDto societyMemberDto = societyService.addUserToSociety(
                new UserIdDto(societyMember.getId()), societyDto.getId());
        Assertions.assertEquals(
                societyService
                        .findSocietyMemberByUserIdAndSocietyId(
                                new UserIdDto(societyMember.getId()),
                                societyDto.getId()),
                societyMemberDto);
    }
}
