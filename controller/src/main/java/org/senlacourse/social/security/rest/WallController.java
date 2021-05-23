package org.senlacourse.social.security.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IWallMessageCommentService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.api.service.IWallService;
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
public class WallController extends AbstractController {

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
                                                                @RequestParam(defaultValue = "0x7fffffff") Integer pageNum,
                                                                @NotNull @PathVariable Long wallId) {
        return new ResponseEntity<>(
                wallMessageService.findAllByWallId(
                        wallId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/{wallId}/messages")
    public ResponseEntity<ResponseMessageDto> addWallMessage(@Validated @RequestBody NewWallMessageDto dto,
                                                             BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        wallMessageService.addNewMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/messages")
    public ResponseEntity<ResponseMessageDto> editWallMessage(@Validated @RequestBody EditMessageDto dto,
                                                              BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        wallMessageService.editWallMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages")
    public ResponseEntity<ResponseMessageDto> removeWallMessage(@RequestParam Long userId,
                                                                @NotNull @RequestParam Long messageId) {
        wallMessageService.deleteByMessageIdAndUserId(messageId, userId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/like")
    public ResponseEntity<ResponseMessageDto> addWallMessageLike(@RequestParam Long userId,
                                                                 @NotNull @RequestParam Long messageId) {
        wallMessageService.addLikeToMessage(userId, messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/dislike")
    public ResponseEntity<ResponseMessageDto> addWallMessageDislike(@RequestParam Long userId,
                                                                    @NotNull @RequestParam Long messageId) {
        wallMessageService.addLikeToMessage(userId, messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/messages/{wallMessageId}/comments")
    public ResponseEntity<Page<WallMessageCommentDto>> getWallMessageComments(@RequestParam(defaultValue = "10") Integer pageSize,
                                                                              @RequestParam(defaultValue = "0x7fffffff") Integer pageNum,
                                                                              @NotNull @PathVariable Long wallMessageId) {
        return new ResponseEntity<>(wallMessageCommentService.findAllByWallMessageId(
                wallMessageId,
                PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> addWallMessageComment(@Validated @RequestBody NewWallMessageCommentDto dto,
                                                                    BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        wallMessageCommentService.addNewWallMessageComment(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> editWallMessageComment(@Validated @RequestBody EditMessageDto dto,
                                                                     BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        wallMessageCommentService.editWallMessageComment(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> removeWallMessageComment(@RequestParam Long userId,
                                                                       @NotNull @RequestParam Long commentId) {
        wallMessageCommentService.deleteByCommentIdAndUserId(commentId, userId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/comments/like")
    public ResponseEntity<ResponseMessageDto> addWallMessageCommentLike(@RequestParam Long userId,
                                                                        @NotNull @RequestParam Long commentId) {
        wallMessageCommentService.addLikeToMessage(userId, commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/messages/comments/dislike")
    public ResponseEntity<ResponseMessageDto> addWallMessageCommentDislike(@RequestParam Long userId,
                                                                           @NotNull @RequestParam Long commentId) {
        wallMessageCommentService.addLikeToMessage(userId, commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

}
