package com.aihealthdiet.controller;

import com.aihealthdiet.entity.HealthGoal;
import com.aihealthdiet.repository.HealthGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goal")
public class HealthGoalController {
    @Autowired
    private HealthGoalRepository healthGoalRepository;

    @PostMapping("/add")
    public HealthGoal addGoal(@RequestBody HealthGoal goal) {
        return healthGoalRepository.save(goal);
    }

    @GetMapping("/user/{userId}")
    public List<HealthGoal> getGoals(@PathVariable Long userId) {
        return healthGoalRepository.findByUserId(userId);
    }
}