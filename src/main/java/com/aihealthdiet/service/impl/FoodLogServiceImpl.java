package com.aihealthdiet.service.impl;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.entity.User;
import com.aihealthdiet.repository.FoodLogRepository;
import com.aihealthdiet.repository.UserRepository;
import com.aihealthdiet.service.AbstractFoodLogService;
import com.aihealthdiet.service.FoodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodLogServiceImpl extends AbstractFoodLogService implements FoodLogService {

    @Autowired
    private FoodLogRepository foodLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public FoodLog addFoodLog(Long userId, FoodLog foodLog) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        foodLog.setUser(user);
        if (foodLog.getEatTime() == null) {
            foodLog.setEatTime(LocalDateTime.now());
        }
        return foodLogRepository.save(foodLog);
    }

    /**
     * 获取用户的所有食物日志
     */
    @Override
    public List<FoodLog> getFoodLogsByUserId(Long userId) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }

        // 调用Repository查询 - 改为 findByUser_Id
        List<FoodLog> logs = foodLogRepository.findByUser_Id(userId);

        // 按进食时间倒序排列
        logs.sort(Comparator.comparing(FoodLog::getEatTime).reversed());
        return logs;
    }

    /**
     * 获取用户当天的食物日志
     */
    @Override
    public List<FoodLog> getTodayFoodLogs(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // 注意方法名：findByUserIdAndEatTimeBetween
        return foodLogRepository.findByUserIdAndEatTimeBetween(userId, startOfDay, endOfDay);
    }

    /**
     * 获取用户最近N天的食物日志
     */
    @Override
    public List<FoodLog> getRecentFoodLogs(Long userId, int days) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("查询天数必须大于0");
        }

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        // 使用已有的 findByUserIdAndDateRange 方法
        return foodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    /**
     * 获取用户指定日期范围内的食物日志
     */
    @Override
    public List<FoodLog> getFoodLogsByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }
        return foodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    /**
     * 统计用户的餐型分布
     */
    @Override
    public Map<String, Long> getMealTypeDistribution(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }

        List<FoodLog> logs = foodLogRepository.findByUser_Id(userId);
        return logs.stream()
                .filter(log -> log.getMealType() != null && !log.getMealType().isEmpty())
                .collect(Collectors.groupingBy(FoodLog::getMealType, Collectors.counting()));
    }

    /**
     * 获取用户今日营养摄入汇总
     */
    @Override
    public Map<String, Double> getTodayNutritionSummary(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<FoodLog> todayLogs = foodLogRepository.findByUserIdAndEatTimeBetween(userId, startOfDay, endOfDay);

        double totalCalories = todayLogs.stream()
                .mapToDouble(log -> log.getCalories() != null ? log.getCalories() : 0.0)
                .sum();

        double totalProtein = todayLogs.stream()
                .mapToDouble(log -> log.getProtein() != null ? log.getProtein() : 0.0)
                .sum();

        double totalCarbs = todayLogs.stream()
                .mapToDouble(log -> log.getCarbs() != null ? log.getCarbs() : 0.0)
                .sum();

        double totalFat = todayLogs.stream()
                .mapToDouble(log -> log.getFat() != null ? log.getFat() : 0.0)
                .sum();

        return Map.of(
                "calories", totalCalories,
                "protein", totalProtein,
                "carbs", totalCarbs,
                "fat", totalFat
        );
    }

    /**
     * 删除食物日志
     */
    @Override
    @Transactional
    public void deleteFoodLog(Long logId, Long userId) {
        FoodLog foodLog = foodLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("食物日志不存在，ID: " + logId));

        if (foodLog.getUser() == null || !foodLog.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除此食物日志");
        }

        foodLogRepository.delete(foodLog);
    }

    /**
     * 更新食物日志
     */
    @Override
    @Transactional
    public FoodLog updateFoodLog(Long logId, Long userId, FoodLog updatedLog) {
        FoodLog existingLog = foodLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("食物日志不存在，ID: " + logId));

        if (existingLog.getUser() == null || !existingLog.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权修改此食物日志");
        }

        // 更新字段
        if (updatedLog.getFoodName() != null) {
            existingLog.setFoodName(updatedLog.getFoodName());
        }
        if (updatedLog.getDescription() != null) {
            existingLog.setDescription(updatedLog.getDescription());
        }
        if (updatedLog.getCalories() != null) {
            existingLog.setCalories(updatedLog.getCalories());
        }
        if (updatedLog.getProtein() != null) {
            existingLog.setProtein(updatedLog.getProtein());
        }
        if (updatedLog.getCarbs() != null) {
            existingLog.setCarbs(updatedLog.getCarbs());
        }
        if (updatedLog.getFat() != null) {
            existingLog.setFat(updatedLog.getFat());
        }
        if (updatedLog.getMealType() != null) {
            existingLog.setMealType(updatedLog.getMealType());
        }
        if (updatedLog.getEatTime() != null) {
            existingLog.setEatTime(updatedLog.getEatTime());
        }

        return foodLogRepository.save(existingLog);
    }
}