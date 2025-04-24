package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.exception.PasswordInvalidException;
import com.compass.demo_park_api.exception.UserNotFoundException;
import com.compass.demo_park_api.exception.UsernameUniqueViolationException;
import com.compass.demo_park_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException(String.format("User %s already exists", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElseThrow(
                () -> new UserNotFoundException(String.format("User id = %d not found", id))
        );
    }

    @Transactional()
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("Passwords are different");
        }

        User user = findById(id);

        if (!currentPassword.equals(user.getPassword())) {
            throw new PasswordInvalidException("Incorrect password");
        }

        user.setPassword(newPassword);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
