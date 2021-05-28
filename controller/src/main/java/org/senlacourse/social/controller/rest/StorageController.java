package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.localstorage.IFileTransportService;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final IFileTransportService fileTransportService;

    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessageDto> uploadFile(@Validated @RequestBody MultipartFile file) {
        fileTransportService.uploadFile(0L, file);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/images")
    public ResponseEntity<Object> downloadFile(@NotNull @RequestParam Long imageId) {
        return fileTransportService.downloadFile(imageId);
    }
}
