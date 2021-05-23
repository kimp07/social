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
    @GetMapping("/messages/{wallMessageId}/comments")
    public ResponseEntity<Page<WallMessageCommentDto>> getWallMessageComments(@RequestParam(defaultValue = "10") Integer pageSize,
                                                                              @RequestParam(defaultValue = "0x7fffffff") Integer pageNum,
                                                                              @NotNull @PathVariable Long wallMessageId) {
        return new ResponseEntity<>(wallMessageCommentService.findAllByWallMessageId(
                wallMessageId,
                PageRequest.of(pageNum, pageSize)),
                HttpStatus.OK);
    }
}
