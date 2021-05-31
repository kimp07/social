package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IWallMessageCommentService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.api.service.IWallService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/walls")
public class WallController {

    private final IWallService wallService;
    private final IWallMessageService wallMessageService;
    private final IWallMessageCommentService wallMessageCommentService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/{societyId}")
    public ResponseEntity<WallDto> getWall(@NotNull @PathVariable Long societyId) {
        return new ResponseEntity<>(
                wallService.findWallBySocietyId(societyId),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping
    public ResponseEntity<WallDto> getRootWall() {
        return new ResponseEntity<>(
                wallService.findRootWall(),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/{wallId}/messages")
    public ResponseEntity<Page<WallMessageDto>> getWallMessages(@RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "0") Integer pageNum,
                                                                @NotNull @PathVariable Long wallId) {
        return new ResponseEntity<>(
                wallMessageService.findAllByWallId(
                        wallId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping("/messages")
    public ResponseEntity<ResponseMessageDto> addWallMessage(@Validated @RequestBody NewWallMessageDto dto,
                                                             BindingResult bindingResult) {
        wallMessageService.addNewMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PutMapping("/messages")
    public ResponseEntity<ResponseMessageDto> editWallMessage(@Validated @RequestBody EditMessageDto dto,
                                                              BindingResult bindingResult) {
        wallMessageService.editWallMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages")
    public ResponseEntity<ResponseMessageDto> removeWallMessage(@RequestParam Long userId,
                                                                @NotNull @RequestParam Long messageId) {
        wallMessageService.deleteByMessageIdAndUserId(new UserIdDto(userId), messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/like")
    public ResponseEntity<ResponseMessageDto> addWallMessageLike(@NotNull @RequestParam Long messageId) {
        wallMessageService.addLikeToMessage(messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/dislike")
    public ResponseEntity<ResponseMessageDto> addWallMessageDislike(@NotNull @RequestParam Long messageId) {
        wallMessageService.addDislikeToMessage(messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/{wallMessageId}/comments")
    public ResponseEntity<Page<WallMessageCommentDto>> getWallMessageComments(@RequestParam(defaultValue = "10") Integer pageSize,
                                                                              @RequestParam(defaultValue = "0") Integer pageNum,
                                                                              @NotNull @PathVariable Long wallMessageId) {
        return new ResponseEntity<>(wallMessageCommentService.findAllByWallMessageId(
                wallMessageId,
                PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> addWallMessageComment(@Validated @RequestBody NewWallMessageCommentDto dto,
                                                                    BindingResult bindingResult) {
        wallMessageCommentService.addNewWallMessageComment(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PutMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> editWallMessageComment(@Validated @RequestBody EditMessageDto dto,
                                                                     BindingResult bindingResult) {
        wallMessageCommentService.editWallMessageComment(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> removeWallMessageComment(@RequestParam Long userId,
                                                                       @NotNull @RequestParam Long commentId) {
        wallMessageCommentService.deleteByCommentIdAndUserId(new UserIdDto(userId), commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/comments/like")
    public ResponseEntity<ResponseMessageDto> addWallMessageCommentLike(@NotNull @RequestParam Long commentId) {
        wallMessageCommentService.addLikeToMessage(commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/comments/dislike")
    public ResponseEntity<ResponseMessageDto> addWallMessageCommentDislike(@NotNull @RequestParam Long commentId) {
        wallMessageCommentService.addDislikeToMessage(commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

}
