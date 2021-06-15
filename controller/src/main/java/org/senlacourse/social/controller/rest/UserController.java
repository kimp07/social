package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.security.IUserSecurityHandlerService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.UpdateUserDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IUserSecurityHandlerService securityHandlerService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping
    public ResponseEntity<Page<UserDto>> showAllUsers(@RequestParam(defaultValue = "10") Integer pageSize,
                                                      @RequestParam(defaultValue = "0") Integer pageNum,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String direction,
                                                      @RequestParam(defaultValue = "") String firstName,
                                                      @RequestParam(defaultValue = "") String surname) {
        return new ResponseEntity<>(
                userService.findAllByFirstNameAndSurname(
                        firstName,
                        surname,
                        PageRequest.of(
                                pageNum,
                                pageSize,
                                Sort.by(Sort.Direction.fromString(direction.toUpperCase(Locale.ROOT)), sortBy))),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER", "ROLE_TEMPORARY"})
    @GetMapping("/cabinet")
    public ResponseEntity<UserDto> getUserInfo() {
        return new ResponseEntity<>(userService.getCurrentAuthorizedUser(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PutMapping("/cabinet")
    public ResponseEntity<ResponseMessageDto> updateUser(@Validated @RequestBody UpdateUserDto dto,
                                                         BindingResult bindingResult) {
        userService.updateUser(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER", "ROLE_TEMPORARY"})
    @ValidatedBindingResult
    @PutMapping("/password")
    public ResponseEntity<ResponseMessageDto> updateUserPassword(@Validated @RequestBody UserPasswordDto dto,
                                                                 BindingResult bindingResult) {
        securityHandlerService.updateUserPassword(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

}
