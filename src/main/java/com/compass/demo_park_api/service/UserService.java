package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.entity.enums.Role;
import com.compass.demo_park_api.exception.PasswordInvalidException;
import com.compass.demo_park_api.exception.UserNotFoundException;
import com.compass.demo_park_api.exception.UsernameUniqueViolationException;
import com.compass.demo_park_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Reader;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional()
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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

        if (!passwordEncoder.matches(currentPassword,user.getPassword())) {
            throw new PasswordInvalidException("Incorrect password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public Role findRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}
