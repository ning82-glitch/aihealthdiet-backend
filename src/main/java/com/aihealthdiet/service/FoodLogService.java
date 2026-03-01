package com.aihealthdiet.service;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.entity.User;
import com.aihealthdiet.repository.FoodLogRepository;
import com.aihealthdiet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public abstract class FoodLogService {

    @Autowired
    private FoodLogRepository foodLogRepository;



    @Autowired
    private UserRepository userRepository;

    public List<FoodLog> getFoodLogsByUserId(Long userId) {
        return foodLogRepository.findByUserId(userId);
    }

    public List<FoodLog> getTodayFoodLogs(Long userId) {
        return foodLogRepository.findByUserIdAndDate(userId, LocalDate.now());
    }

    public List<FoodLog> getRecentFoodLogs(Long userId, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return foodLogRepository.findRecentByUserId(userId, startDate);
    }

    @Transactional
    public FoodLog addFoodLog(Long userId, FoodLog foodLog) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        foodLog.setUser(user);
        if (foodLog.getEatTime() == null) {
            foodLog.setEatTime(LocalDateTime.now());
        }

        return foodLogRepository.save(foodLog);
    }

    @Transactional
    public void deleteFoodLog(Long logId, Long userId) {
        FoodLog foodLog = foodLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));

        if (!foodLog.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除此记录");
        }

        foodLogRepository.delete(foodLog);
    }

    public Map<String, Double> getTodayNutritionSummary(Long userId) {
        List<FoodLog> todayLogs = getTodayFoodLogs(userId);

        double totalCalories = todayLogs.stream()
                .mapToDouble(f -> f.getCalories() != null ? f.getCalories() : 0)
                .sum();

        double totalProtein = todayLogs.stream()
                .mapToDouble(f -> f.getProtein() != null ? f.getProtein() : 0)
                .sum();

        double totalCarbs = todayLogs.stream()
                .mapToDouble(f -> f.getCarbs() != null ? f.getCarbs() : 0)
                .sum();

        double totalFat = todayLogs.stream()
                .mapToDouble(f -> f.getFat() != null ? f.getFat() : 0)
                .sum();

        return Map.of(
                "calories", totalCalories,
                "protein", totalProtein,
                "carbs", totalCarbs,
                "fat", totalFat
        );
    }

    public Map<String, Long> getMealTypeDistribution(Long userId) {
        List<FoodLog> logs = getRecentFoodLogs(userId, 30);
        return logs.stream()
                .collect(Collectors.groupingBy(
                        FoodLog::getMealType,
                        Collectors.counting()
                ));
    }

    public abstract List<FoodLog> getFoodLogsByUser(Long userId);
}