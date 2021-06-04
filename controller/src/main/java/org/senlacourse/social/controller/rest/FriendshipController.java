package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.FriendshipMemberDto;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    @PostMapping("/requests/{id}")
    public ResponseEntity<ResponseMessageDto> sendFriendshipRequest(@PathVariable Long id) {
        friendshipRequestService.saveNewFriendshipRequest(new NewFriendshipRequestDto().setRecipientId(id));
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/requests/{id}")
    public ResponseEntity<ResponseMessageDto> confirmFriendshipRequest(@NotNull @PathVariable Long id) {
        friendshipRequestService.confirmFriendshipRequestById(id);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<ResponseMessageDto> declineFriendshipRequest(@NotNull @PathVariable Long id) {
        friendshipRequestService.deleteById(id);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/fiends/{id}")
    public ResponseEntity<Page<FriendshipMemberDto>> getFriends(@PathVariable Long id,
                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                friendshipService.findAllFriendshipMembersByUserId(new UserIdDto(id), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/requests/in/{id}")
    public ResponseEntity<Page<FriendshipRequestDto>> getInRequests(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                friendshipRequestService.findAllByRecipientId(new UserIdDto(id), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/requests/out/{id}")
    public ResponseEntity<Page<FriendshipRequestDto>> getOutRequests(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "0") Integer pageNum) {
        return new ResponseEntity<>(
                friendshipRequestService.findAllBySenderId(new UserIdDto(id), PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }
}
