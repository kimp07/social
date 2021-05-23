package org.senlacourse.social.security.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.dto.NewSocietyDto;
import org.senlacourse.social.dto.ResponseMessageDto;
import org.senlacourse.social.dto.SocietyDto;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/societies")
public class SocietyController extends AbstractController {

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
    @PostMapping
    public ResponseEntity<SocietyDto> createSociety(@Validated @RequestBody NewSocietyDto dto,
                                                    BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        return new ResponseEntity<>(
                societyService.createNewSociety(dto),
                HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping("/members/{societyId}/{userId}")
    public ResponseEntity<ResponseMessageDto> addUserToSociety(@NotNull @PathVariable Long societyId,
                                                               @PathVariable Long userId) {
        societyService.addUserToSociety(userId, societyId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_USER"})
    @DeleteMapping("/members/{societyId}/{userId}")
    public ResponseEntity<ResponseMessageDto> removeUserFromSociety(@NotNull @PathVariable Long societyId,
                                                                    @PathVariable Long userId) {
        societyService.removeUserFromSociety(userId, societyId);
        return new ResponseEntity<>(new ResponseMessageDto(), HttpStatus.OK);
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
