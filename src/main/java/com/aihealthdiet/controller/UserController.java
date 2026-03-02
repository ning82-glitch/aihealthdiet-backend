package com.aihealthdiet.controller;

import com.aihealthdiet.entity.User;
import com.aihealthdiet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findByUsername(""); // 这里简化，实际应该通过ID查找
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "height", user.getHeight(),
                "weight", user.getWeight(),
                "targetWeight", user.getTargetWeight(),
                "dailyCalorieGoal", user.getDailyCalorieGoal(),
                "allergies", user.getAllergies(),
                "preferredCuisine", user.getPreferredCuisine()
        ));
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long userId,
            @RequestBody User updatedUser) {
        User updated = userService.updateProfile(userId, updatedUser);
        return ResponseEntity.ok(updated);
    }
}