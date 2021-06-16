package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.FriendshipDto;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final IFriendshipRequestService friendshipRequestService;
    private final IFriendshipService friendshipService;

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping("/requests")
    public ResponseEntity<ResponseMessageDto> sendFriendshipRequest(@Validated @RequestBody NewFriendshipRequestDto dto,
                                                                    BindingResult bindingResult) {
        friendshipRequestService.saveNewFriendshipRequest(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/requests")
    public ResponseEntity<ResponseMessageDto> confirmFriendshipRequest(@NotNull @RequestParam Long requestId,
                                                                       @RequestParam(defaultValue = "0") Long userId) {
        friendshipRequestService.confirmFriendshipRequestById(new UserIdDto(userId), requestId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/requests")
    public ResponseEntity<ResponseMessageDto> declineFriendshipRequest(@NotNull @RequestParam Long requestId,
                                                                       @RequestParam(defaultValue = "0") Long userId) {
        friendshipRequestService.decline(new UserIdDto(userId), requestId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/friends")
    public ResponseEntity<Page<FriendshipDto>> getFriends(@RequestParam(defaultValue = "0") Long userId,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                friendshipService.findAllByUserId(new UserIdDto(userId), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/requests/in")
    public ResponseEntity<Page<FriendshipRequestDto>> getInRequests(@RequestParam(defaultValue = "0") Long userId,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                friendshipRequestService.findAllByRecipientId(new UserIdDto(userId), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/requests/out")
    public ResponseEntity<Page<FriendshipRequestDto>> getOutRequests(@RequestParam(defaultValue = "0") Long userId,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                friendshipRequestService.findAllBySenderId(new UserIdDto(userId), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }
}
