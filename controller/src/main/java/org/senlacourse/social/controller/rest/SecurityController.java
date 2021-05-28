package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.security.IUserSecurityHandlerService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.AuthDto;
import org.senlacourse.social.dto.EmailDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SecurityController {

    private static final String OPERATION_COMPLETED = "Operation completed";

    private final IUserSecurityHandlerService securityHandlerService;

    @ValidatedBindingResult
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessageDto> signUp(@Validated @RequestBody NewUserDto dto,
                                                      BindingResult bindingResult) {
        securityHandlerService.saveUser(dto);
        return new ResponseEntity<>(new ResponseMessageDto(OPERATION_COMPLETED), HttpStatus.OK);
    }

    @ValidatedBindingResult
    @PostMapping("/signin")
    public ResponseEntity<ResponseMessageDto> signIn(@Validated @RequestBody AuthDto dto,
                                                     BindingResult bindingResult) {
        return new ResponseEntity<>(
                new ResponseMessageDto(
                        securityHandlerService.getUserToken(dto, false)),
                HttpStatus.OK);
    }

    @GetMapping("/repair")
    public ResponseEntity<ResponseMessageDto> restoreAccess(@Validated @RequestBody EmailDto dto) {
        return new ResponseEntity<>(
                new ResponseMessageDto(
                        securityHandlerService.restoreAssess(dto)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/refresh")
    public ResponseEntity<ResponseMessageDto> refreshToken() {
        return new ResponseEntity<>(
                new ResponseMessageDto(
                        securityHandlerService.refreshUserToken(false)),
                HttpStatus.OK);
    }
}
