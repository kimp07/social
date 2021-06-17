package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.CorrespondenceDto;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.projection.IUnreadTalkMessagesView;
import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdCountView;
import org.senlacourse.social.service.ICorrespondenceService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talks")
public class TalkController {

    private final ITalkService talkService;
    private final ITalkMessageService talkMessageService;
    private final ICorrespondenceService correspondenceService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping
    public ResponseEntity<Page<TalkDto>> getAllUserTalks(@RequestParam(defaultValue = "10") Integer pageSize,
                                                         @RequestParam(defaultValue = "0") Integer pageNum,
                                                         @RequestParam(defaultValue = "") Long userId) {
        return new ResponseEntity<>(
                talkService.findAllByUserId(
                        userId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping
    public ResponseEntity<TalkDto> addNewTalk(@Validated @RequestBody NewTalkDto dto,
                                              BindingResult bindingResult) {
        return new ResponseEntity<>(
                talkService.addNewTalk(dto),
                HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/{talkId}/members")
    public ResponseEntity<ResponseMessageDto> addUserToTalk(@NotNull @PathVariable Long talkId,
                                                            @NotNull @RequestParam(defaultValue = "0") Long userId,
                                                            @NotNull @RequestParam Long memberId) {
        talkService.addTalkMemberToTalk(new UserIdDto(userId), talkId, memberId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/{talkId}/members")
    public ResponseEntity<ResponseMessageDto> removeUserFromTalk(@NotNull @PathVariable Long talkId,
                                                                 @NotNull @RequestParam(defaultValue = "0") Long userId) {
        talkService.removeTalkMemberFromTalk(new UserIdDto(userId), talkId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/{talkId}/messages")
    public ResponseEntity<Page<CorrespondenceDto>> getTalkMessages(@RequestParam(defaultValue = "0") Long userId,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "0") Integer pageNum,
                                                                   @NotNull @PathVariable Long talkId) {
        return new ResponseEntity<>(
                correspondenceService.findAllByUserIdAndTalkId(
                                new UserIdDto(userId),
                                talkId,
                                PageRequest.of(pageNum, pageSize)),
                        HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping("/messages")
    public ResponseEntity<ResponseMessageDto> addNewMessage(@Validated @RequestBody NewTalkMessageDto dto,
                                                            BindingResult bindingResult) {
        talkMessageService.addNewMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/unread")
    public ResponseEntity<Page<UnreadTalkMessagesGroupByTalkIdCountView>> getUnreadMessagesGroupByTalk(
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "0") Long userId,
            @RequestParam Long talkId) {
        return new ResponseEntity<>(
                correspondenceService.getCountUnreadMessagesByUserIdGroupByTalkId(
                        new UserIdDto(userId),
                        talkId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/unread/count")
    public ResponseEntity<IUnreadTalkMessagesView> getUnreadMessages(@RequestParam(defaultValue = "0") Long userId) {
        return new ResponseEntity<>(
                correspondenceService.getCountUnreadMessagesByUserId(
                        new UserIdDto(userId)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages/{talkId}/unread")
    public ResponseEntity<ResponseMessageDto> removeUnreadMessages(@NotNull @PathVariable Long talkId,
                                                                   @RequestParam(defaultValue = "0") Long userId) {
        correspondenceService.updateMessagesSetUnreadFalseByRecipientIdAndTalkId(new UserIdDto(userId), talkId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages/unread")
    public ResponseEntity<ResponseMessageDto> removeUnreadMessages(@RequestParam(defaultValue = "0") Long userId) {
        correspondenceService.updateMessagesSetUnreadFalseByRecipientId(new UserIdDto(userId));
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

}
