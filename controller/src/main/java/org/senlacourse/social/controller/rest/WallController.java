package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IWallMessageCommentService;
import org.senlacourse.social.api.service.IWallMessageService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageCommentDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.WallMessageCommentDto;
import org.senlacourse.social.dto.WallMessageDto;
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
@RequiredArgsConstructor
@RequestMapping("/walls")
public class WallController {

    private final IWallMessageService wallMessageService;
    private final IWallMessageCommentService wallMessageCommentService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/{wallId}/messages")
    public ResponseEntity<Page<WallMessageDto>> getWallMessages(@RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "0") Integer pageNum,
                                                                @NotNull @PathVariable Long societyId) {
        return new ResponseEntity<>(
                wallMessageService.findAllBySocietyId(
                        societyId,
                        PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping("/messages")
    public ResponseEntity<ResponseMessageDto> addWallMessage(@Validated @RequestBody NewWallMessageDto dto,
                                                             BindingResult bindingResult) {
        wallMessageService.addNewMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PutMapping("/messages")
    public ResponseEntity<ResponseMessageDto> editWallMessage(@Validated @RequestBody EditMessageDto dto,
                                                              BindingResult bindingResult) {
        wallMessageService.editWallMessage(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages")
    public ResponseEntity<ResponseMessageDto> removeWallMessage(@RequestParam Long userId,
                                                                @NotNull @RequestParam Long messageId) {
        wallMessageService.deleteByMessageIdAndUserId(new UserIdDto(userId), messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/messages/like")
    public ResponseEntity<ResponseMessageDto> addWallMessageLike(@NotNull @RequestParam Long messageId) {
        wallMessageService.addLikeToMessage(messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/messages/dislike")
    public ResponseEntity<ResponseMessageDto> addWallMessageDislike(@NotNull @RequestParam Long messageId) {
        wallMessageService.addDislikeToMessage(messageId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
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
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PutMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> editWallMessageComment(@Validated @RequestBody EditMessageDto dto,
                                                                     BindingResult bindingResult) {
        wallMessageCommentService.editWallMessageComment(dto);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/messages/comments")
    public ResponseEntity<ResponseMessageDto> removeWallMessageComment(@RequestParam Long userId,
                                                                       @NotNull @RequestParam Long commentId) {
        wallMessageCommentService.deleteByCommentIdAndUserId(new UserIdDto(userId), commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/messages/comments/like")
    public ResponseEntity<ResponseMessageDto> addWallMessageCommentLike(@NotNull @RequestParam Long commentId) {
        wallMessageCommentService.addLikeToMessage(commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @PutMapping("/messages/comments/dislike")
    public ResponseEntity<ResponseMessageDto> addWallMessageCommentDislike(@NotNull @RequestParam Long commentId) {
        wallMessageCommentService.addDislikeToMessage(commentId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

}
