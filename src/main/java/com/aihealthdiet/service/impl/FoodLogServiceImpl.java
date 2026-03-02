package com.aihealthdiet.service.impl;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.entity.User;
import com.aihealthdiet.repository.FoodLogRepository;
import com.aihealthdiet.repository.UserRepository;
import com.aihealthdiet.service.AbstractFoodLogService;
import com.aihealthdiet.service.FoodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Spring Boot 2.x
// import jakarta.transaction.Transactional; // Spring Boot 3+

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodLogServiceImpl extends AbstractFoodLogService implements FoodLogService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodLogRepository foodLogRepository;


    public FoodLog addFoodLog(Long userId, FoodLog foodLog) {
        // 1. 根据 userId 查询 User 对象
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 将 User 对象设置到 FoodLog 中
        foodLog.setUser(user);  // 现在设置的是 user 对象，而不是 userId

        // 3. 保存 FoodLog
        return foodLogRepository.save(foodLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodLog> getFoodLogsByUserId(Long userId) {
        return foodLogRepository.findByUserId(userId);
    }

    @Override
    public List<FoodLog> getTodayFoodLogs(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return foodLogRepository.findByUserIdAndEatTimeBetween(userId, startOfDay, endOfDay);
    }

    @Override
    public List<FoodLog> getRecentFoodLogs(Long userId, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("查询天数必须大于0");
        }
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        return foodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public List<FoodLog> getFoodLogsByUser(Long userId) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空且必须大于0");
        }
        // 调用Repository查询
        List<FoodLog> logs = foodLogRepository.findByUserId(userId);
        // 可选：按进食时间倒序排列（如果Repository未排序）
        logs.sort(Comparator.comparing(FoodLog::getEatTime).reversed());
        return logs;
    }

    @Override
    public List<FoodLog> getRecentFoodLogsByUser(Long userId, int recentDays) {
        if (recentDays <= 0) {
            throw new IllegalArgumentException("查询天数必须大于0");
        }
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(recentDays);
        return foodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    // 其他接口方法（如 getMealTypeDistribution、getTodayNutritionSummary）根据实际需求实现
    @Override
    public Map<String, Long> getMealTypeDistribution(Long userId) {
        // 示例：统计用户的餐型分布（需结合Repository逻辑）
        List<FoodLog> logs = foodLogRepository.findByUserId(userId);
        return logs.stream()
                .collect(Collectors.groupingBy(FoodLog::getMealType, Collectors.counting()));
    }

    @Override
    public Map<String, Double> getTodayNutritionSummary(Long userId) {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        List<FoodLog> todayLogs = foodLogRepository.findByUserIdAndEatTimeBetween(userId, startOfDay, endOfDay);
        // 假设 FoodLog 有 getCalories()、getProtein() 等方法
        double totalCalories = todayLogs.stream().mapToDouble(FoodLog::getCalories).sum();
        double totalProtein = todayLogs.stream().mapToDouble(FoodLog::getProtein).sum();
        // 可扩展其他营养成分
        return Map.of(
                "calories", totalCalories,
                "protein", totalProtein
        );
    }

    @Override
    public void deleteFoodLog(Long logId, Long userId) {

    }
}

