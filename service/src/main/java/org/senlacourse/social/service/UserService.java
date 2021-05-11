package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.stereotype.Service;
import senlacourse.social.domain.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUserLogin(String userLogin) {
        return userRepository.findOneByUserLogin(userLogin);
    }

}
