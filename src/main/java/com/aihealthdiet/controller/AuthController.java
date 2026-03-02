package com.aihealthdiet.controller;

import com.aihealthdiet.entity.User;
import com.aihealthdiet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "注册成功",
                    "userId", registeredUser.getId(),
                    "username", registeredUser.getUsername()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "用户不存在"
            ));
        }

        User user = userOpt.get();
        if (!userService.verifyPassword(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "密码错误"
            ));
        }

        // 这里应该生成JWT token，简化处理
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "登录成功",
                "user", Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "nickname", user.getNickname(),
                        "height", user.getHeight(),
                        "weight", user.getWeight(),
                        "targetWeight", user.getTargetWeight(),
                        "dailyCalorieGoal", user.getDailyCalorieGoal()
                )
        ));
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}