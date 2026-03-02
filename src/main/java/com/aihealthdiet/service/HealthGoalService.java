package com.aihealthdiet.service;

import com.aihealthdiet.entity.HealthGoal;
import com.aihealthdiet.entity.User;
import com.aihealthdiet.repository.HealthGoalRepository;
import com.aihealthdiet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HealthGoalService {

    @Autowired
    private HealthGoalRepository healthGoalRepository;

    @Autowired
    private UserRepository userRepository;

    public List<HealthGoal> getHealthGoalsByUserId(Long userId) {
        return healthGoalRepository.findByUserId(userId);
    }

    public Optional<HealthGoal> getCurrentHealthGoal(Long userId) {
        List<HealthGoal> goals = getHealthGoalsByUserId(userId);
        if (goals.isEmpty()) {
            return Optional.empty();
        }
        // 返回最新的目标
        return Optional.of(goals.get(goals.size() - 1));
    }

    @Transactional
    public HealthGoal setHealthGoal(Long userId, HealthGoal healthGoal) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        healthGoal.setUser(user);
        if (healthGoal.getStartDate() == null) {
            healthGoal.setStartDate(LocalDate.now());
        }

        return healthGoalRepository.save(healthGoal);
    }

    public Map<String, Object> calculateProgress(Long userId) {
        Optional<HealthGoal> goalOpt = getCurrentHealthGoal(userId);
        if (goalOpt.isEmpty()) {
            return Map.of("hasGoal", false, "message", "尚未设置健康目标");
        }

        HealthGoal goal = goalOpt.get();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        double currentWeight = user.getWeight() != null ? user.getWeight() : 0;
        double targetWeight = goal.getTargetWeight();
        double initialWeight = goal.getInitialWeight();

        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getTargetDate());
        long daysPassed = ChronoUnit.DAYS.between(goal.getStartDate(), LocalDate.now());

        double totalWeightToLose = initialWeight - targetWeight;
        double currentWeightLost = initialWeight - currentWeight;

        double progressPercentage = (currentWeightLost / totalWeightToLose) * 100;
        double expectedWeightLost = (daysPassed / (double) totalDays) * totalWeightToLose;

        String status = "正常";
        if (currentWeightLost < expectedWeightLost * 0.8) {
            status = "落后";
        } else if (currentWeightLost > expectedWeightLost * 1.2) {
            status = "超前";
        }

        return Map.of(
                "hasGoal", true,
                "goalType", goal.getGoalType(),
                "currentWeight", currentWeight,
                "targetWeight", targetWeight,
                "initialWeight", initialWeight,
                "progressPercentage", Math.min(100, Math.max(0, progressPercentage)),
                "daysPassed", daysPassed,
                "totalDays", totalDays,
                "status", status
        );
    }
}