package com.aihealthdiet.service;

import com.aihealthdiet.entity.FoodLog;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FoodLogService {

    FoodLog addFoodLog(Long userId, FoodLog foodLog);

    List<FoodLog> getFoodLogsByUserId(Long userId);  // 这个方法需要

    List<FoodLog> getTodayFoodLogs(Long userId);

    List<FoodLog> getRecentFoodLogs(Long userId, int days);  // 这个方法需要

    List<FoodLog> getFoodLogsByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    Map<String, Long> getMealTypeDistribution(Long userId);

    Map<String, Double> getTodayNutritionSummary(Long userId);

    void deleteFoodLog(Long logId, Long userId);

    FoodLog updateFoodLog(Long logId, Long userId, FoodLog updatedLog);
}
