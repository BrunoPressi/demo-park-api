package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional()
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElseThrow(
                () -> new RuntimeException("User not found!")
        );
    }

    @Transactional()
    public User updatePassword(Long id, String password) {
        User user = findById(id);
        user.setPassword(password);
        return user;
    }

}
