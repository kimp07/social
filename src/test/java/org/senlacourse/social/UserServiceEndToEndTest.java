package org.senlacourse.social;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceEndToEndTest {

    @Autowired
    private IUserService userService;

    private NewUserDto userDto;
    private NewUserDto updateUserDto;

    @BeforeEach
    public void setUp() {
        userDto = new NewUserDto()
                .setLogin("user")
                .setFirstName("FirstName")
                .setSurname("Surname")
                .setEmail("email@mail.com")
                .setPassword("password")
                .setBirthDate("1990-02-15")
                .setAboutMe("")
                .setRoleId(1L);

        updateUserDto = new NewUserDto()
                .setLogin("user2")
                .setFirstName("FirstName")
                .setSurname("Surname")
                .setEmail("email2@mail.com")
                .setBirthDate("1990-02-15")
                .setPassword("password")
                .setAboutMe("")
                .setRoleId(1L);
    }


    @Test
    void addNewUser() {
        UserDto userFromBase = userService
                .saveUser(userDto);
        Assertions.assertNotNull(
                userService
                        .findById(
                                new UserIdDto(userFromBase.getId())));
    }

    @Test
    void addNewUserFailedWithNullPassword() {
        userDto.setPassword(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void updateUser() {
        UserDto userFromBase = userService
                .saveUser(updateUserDto);
        userFromBase.setAboutMe("About me");
        userService.updateUser(userFromBase);
        Assertions.assertEquals("About me",
                userService.
                        findById(
                                new UserIdDto(
                                        userFromBase.getId())
                        ).getAboutMe());
    }
}
