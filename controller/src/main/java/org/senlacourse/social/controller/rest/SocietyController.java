package org.senlacourse.social.controller.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.validation.ValidatedBindingResult;
import org.senlacourse.social.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/societies")
public class SocietyController {

    private final ISocietyService societyService;

    @Secured(value = {"ROLE_USER"})
    @GetMapping
    public ResponseEntity<Page<SocietyDto>> getAllSocieties(@RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "0") Integer pageNum,
                                                            @RequestParam String title,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(
                societyService.findAll(
                        title,
                        PageRequest.of(
                                pageNum,
                                pageSize,
                                Sort.by(Sort.Direction.fromString(direction.toUpperCase(Locale.ROOT)), sortBy))),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<SocietyDto> getById(@NotNull @PathVariable Long id) {
        SocietyDto societyDto = societyService.findById(id);
        return new ResponseEntity<>(
                societyDto,
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @ValidatedBindingResult
    @PostMapping
    public ResponseEntity<SocietyDto> createSociety(@Validated @RequestBody NewSocietyDto dto,
                                                    BindingResult bindingResult) {
        SocietyDto societyDto = societyService.createNewSociety(dto);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
                UriComponentsBuilder.fromPath("/societies/{id}").buildAndExpand(societyDto.getId()).toUri());
        return new ResponseEntity<>(
                societyDto,
                responseHeaders,
                HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/members/{societyId}")
    public ResponseEntity<ResponseMessageDto> addUserToSociety(@NotNull @PathVariable Long societyId,
                                                               @RequestParam(defaultValue = "0") Long userId) {
        societyService.addUserToSociety(new UserIdDto(userId), societyId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/members/{societyId}")
    public ResponseEntity<ResponseMessageDto> removeUserFromSociety(@NotNull @PathVariable Long societyId,
                                                                    @RequestParam(defaultValue = "0") Long userId) {
        societyService.removeUserFromSociety(new UserIdDto(userId), societyId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.NO_CONTENT);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping("/members/{societyId}")
    public ResponseEntity<Page<SocietyMemberDto>> getAllSocietyMembers(@NotNull @PathVariable Long societyId,
                                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                                       @RequestParam(defaultValue = "0") Integer pageNum,
                                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                                       @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(
                societyService.findAllSocietyMembersBySocietyId(
                        societyId,
                        PageRequest.of(
                                pageNum,
                                pageSize,
                                Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy))),
                HttpStatus.OK);
    }
}
