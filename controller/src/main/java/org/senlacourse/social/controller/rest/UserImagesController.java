package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IUserImageService;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.UserImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class UserImagesController {

    private final IUserImageService userImageService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping
    public ResponseEntity<Page<UserImageDto>> getUserImages(@RequestParam(defaultValue = "0") Long userId,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                userImageService
                        .findAllImagesByUserId(new UserIdDto(userId), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/image")
    public ResponseEntity<UserImageDto> getUserImage(@RequestParam(defaultValue = "0") Long userId,
                                                     @NotNull @RequestParam Long imageId) {
        return new ResponseEntity<>(
                userImageService
                        .findByUserIdAndImageId(new UserIdDto(userId), imageId),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/image")
    public ResponseEntity<UserImageDto> addUserImage(@RequestParam(defaultValue = "0") Long userId,
                                                     @NotNull @RequestParam Long imageId) {
        return new ResponseEntity<>(
                userImageService
                        .save(new UserIdDto(userId), imageId),
                HttpStatus.OK);
    }


    @Secured(value = {"ROLE_USER"})
    @PutMapping("/avatar")
    public ResponseEntity<ResponseMessageDto> setImageToAvatar(@RequestParam(defaultValue = "0") Long userId,
                                                               @NotNull @RequestParam Long imageId) {
        userImageService.setImageToUserAvatar(new UserIdDto(userId), imageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping
    public ResponseEntity<ResponseMessageDto> deleteUserImage(@RequestParam(defaultValue = "0") Long userId,
                                                              @NotNull @RequestParam Long imageId) {
        userImageService.deleteByUserImageIdAndUserId(new UserIdDto(userId), imageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }
}
