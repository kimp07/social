package org.senlacourse.social.security.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.ITalkMessageService;
import org.senlacourse.social.api.service.ITalkService;
import org.senlacourse.social.domain.projection.ITalkMessagesCacheTalksCountView;
import org.senlacourse.social.dto.NewTalkDto;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.TalkDto;
import org.senlacourse.social.dto.TalkMessageDto;
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
public class TalkController extends AbstractController {

    private final ITalkService talkService;
    private final ITalkMessageService talkMessageService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping
    public ResponseEntity<Page<TalkDto>> getAllUserTalks(@RequestParam(defaultValue = "10") Integer pageSize,
                                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "") Long[] userId) {
        return new ResponseEntity<>(
                talkService.findAllByUserIds(
                        userId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping
    public ResponseEntity<TalkDto> addNewTalk(@Validated @RequestBody NewTalkDto dto,
                                              BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        return new ResponseEntity<>(
                talkService.addNewTalk(dto),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/talks/{talkId}/members")
    public ResponseEntity<ResponseMessageDto> addUserToTalk(@NotNull @PathVariable Long talkId,
                                                            @NotNull @RequestParam Long userId) {
        talkService.addTalkMemberToTalk(talkId, userId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/talks/{talkId}/members")
    public ResponseEntity<ResponseMessageDto> removeUserFromTalk(@NotNull @PathVariable Long talkId,
                                                                 @NotNull @RequestParam Long userId) {
        talkService.removeTalkMemberFromTalk(talkId, userId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/{talkId}")
    public ResponseEntity<Page<TalkMessageDto>> getTalkMessages(@RequestParam(defaultValue = "10") Integer pageSize,
                                                                @NotNull @PathVariable Long talkId) {
        return new ResponseEntity<>(
                talkMessageService.findAllByTalkId(
                        talkId,
                        PageRequest.of(1, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages")
    public ResponseEntity<ResponseMessageDto> addNewMessage(@Validated @RequestBody NewTalkMessageDto dto,
                                                            BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        talkMessageService.addNewMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/{talkId}/cache")
    public ResponseEntity<ITalkMessagesCacheTalksCountView> getUnreadedMessages(@NotNull @PathVariable Long talkId,
                                                                                @RequestParam Long userId) {
        return new ResponseEntity<>(
                talkMessageService.findCacheMessagesCountByRecipientIdAndTalkId(userId, talkId),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/cache")
    public ResponseEntity<Page<ITalkMessagesCacheTalksCountView>> getUnreadedMessages(
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam Long userId) {
        return new ResponseEntity<>(
                talkMessageService.findCacheMessagesByRecipientIdGroupByTalkId(
                        userId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }
}
