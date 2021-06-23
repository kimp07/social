package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IImageService;
import org.senlacourse.social.dto.ImageDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImagesController {

    private final IImageService imageService;

    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> uploadImage(@Validated @RequestBody MultipartFile file) {
        ImageDto dto = imageService.save(file);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
                UriComponentsBuilder.fromPath("/images/{id}").buildAndExpand(dto.getId()).toUri());
        return new ResponseEntity<>(
                dto,
                responseHeaders,
                HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping()
    public ResponseEntity<Object> downloadFile(@NotNull @RequestParam Long imageId) {
        return imageService.getImageFileById(imageId);
    }


}
