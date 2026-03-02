package com.aihealthdiet.service;

import com.aihealthdiet.entity.FoodLog;
import java.util.List;
import java.util.Map;


public interface FoodLogService {

    // 对应 FoodLogServiceImpl 中的 addFoodLog 方法
    FoodLog addFoodLog(Long userId, FoodLog foodLog);

    // 对应 FoodLogServiceImpl 中的 getFoodLogsByUser 方法
    List<FoodLog> getFoodLogsByUser(Long userId);

    // 对应 FoodLogServiceImpl 中的 getTodayFoodLogs 方法
    List<FoodLog> getTodayFoodLogs(Long userId);

    // 对应 FoodLogServiceImpl 中的 getRecentFoodLogs 方法
    List<FoodLog> getRecentFoodLogs(Long userId, int days);

    // 对应 FoodLogServiceImpl 中的 getMealTypeDistribution 方法
    Map<String, Long> getMealTypeDistribution(Long userId);

    // 对应 FoodLogServiceImpl 中的 getTodayNutritionSummary 方法
    Map<String, Double> getTodayNutritionSummary(Long userId);

    // 对应 FoodLogServiceImpl 中的 deleteFoodLog 方法
    void deleteFoodLog(Long logId, Long userId);

    List<FoodLog> getFoodLogsByUserId(Long userId);
}

