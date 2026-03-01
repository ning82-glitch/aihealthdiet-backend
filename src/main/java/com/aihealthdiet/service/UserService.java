package com.aihealthdiet.service;

import com.aihealthdiet.entity.User;
import com.aihealthdiet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User register(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User updateProfile(Long userId, User updatedUser) {
        return userRepository.findById(userId).map(user -> {
            if (updatedUser.getNickname() != null) {
                user.setNickname(updatedUser.getNickname());
            }
            if (updatedUser.getHeight() != null) {
                user.setHeight(updatedUser.getHeight());
            }
            if (updatedUser.getWeight() != null) {
                user.setWeight(updatedUser.getWeight());
            }
            if (updatedUser.getTargetWeight() != null) {
                user.setTargetWeight(updatedUser.getTargetWeight());
            }
            if (updatedUser.getDailyCalorieGoal() != null) {
                user.setDailyCalorieGoal(updatedUser.getDailyCalorieGoal());
            }
            if (updatedUser.getAllergies() != null) {
                user.setAllergies(updatedUser.getAllergies());
            }
            if (updatedUser.getPreferredCuisine() != null) {
                user.setPreferredCuisine(updatedUser.getPreferredCuisine());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}