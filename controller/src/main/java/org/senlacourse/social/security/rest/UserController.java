package org.senlacourse.social.security.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.security.IUserSecurityHandlerService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.UpdateUserDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends AbstractController {

    private final IUserService userService;
    private final IUserSecurityHandlerService securityHandlerService;

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<Page<UserDto>> showAllUsers(@RequestParam(defaultValue = "1") Integer pageSize,
                                                      @RequestParam(defaultValue = "10") Integer pageNum) {
        return new ResponseEntity<>(userService.findAll(PageRequest.of(pageNum, pageSize)), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/cabinet")
    public ResponseEntity<UserDto> getUserInfo() {
        return new ResponseEntity<>(userService.getCurrentAuthorizedUser(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/update")
    public ResponseEntity<ResponseMessageDto> updateUser(@Validated @RequestBody UpdateUserDto dto,
                                                         BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        userService.updateUser(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/updatePassword")
    public ResponseEntity<ResponseMessageDto> updateUserPassword(@Validated @RequestBody UserPasswordDto dto,
                                                                 BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        securityHandlerService.updateUserPassword(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }
}
