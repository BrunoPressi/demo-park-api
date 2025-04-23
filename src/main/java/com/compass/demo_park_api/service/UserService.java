package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords are different");
        }

        User user = findById(id);

        if (!currentPassword.equals(user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        user.setPassword(newPassword);
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
