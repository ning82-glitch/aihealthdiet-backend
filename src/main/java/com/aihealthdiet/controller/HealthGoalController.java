package com.aihealthdiet.controller;

import com.aihealthdiet.entity.HealthGoal;
import com.aihealthdiet.service.HealthGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/healthgoal")
public class HealthGoalController {

    @Autowired
    private HealthGoalService healthGoalService;

    @GetMapping("/list")
    public ResponseEntity<List<HealthGoal>> listHealthGoals(@RequestParam Long userId) {
        List<HealthGoal> goals = healthGoalService.getHealthGoalsByUserId(userId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> currentHealthGoal(@RequestParam Long userId) {
        return healthGoalService.getCurrentHealthGoal(userId)
                .map(goal -> ResponseEntity.ok(Map.of(
                        "hasGoal", true,
                        "goal", goal
                )))
                .orElse(ResponseEntity.ok(Map.of(
                        "hasGoal", false,
                        "message", "未设置健康目标"
                )));
    }

    @PostMapping("/set")
    public ResponseEntity<HealthGoal> setHealthGoal(
            @RequestParam Long userId,
            @RequestBody HealthGoal healthGoal) {
        HealthGoal savedGoal = healthGoalService.setHealthGoal(userId, healthGoal);
        return ResponseEntity.ok(savedGoal);
    }

    @GetMapping("/progress")
    public ResponseEntity<Map<String, Object>> calculateProgress(@RequestParam Long userId) {
        Map<String, Object> progress = healthGoalService.calculateProgress(userId);
        return ResponseEntity.ok(progress);
    }
}