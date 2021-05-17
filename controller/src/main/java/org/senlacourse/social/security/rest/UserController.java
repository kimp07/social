package org.senlacourse.social.security.rest;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> showAllUsers(@RequestParam(defaultValue = "1") Integer pageSize,
                                                      @RequestParam(defaultValue = "10") Integer pageNum) {
        return new ResponseEntity<>(userService.findAll(PageRequest.of(pageNum, pageSize)), HttpStatus.OK);
    }
}
