package com.aihealthdiet.service;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.repository.FoodLogRepository;
import com.aihealthdiet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class AbstractFoodLogService {

    @Autowired
    protected FoodLogRepository foodLogRepository;

    @Autowired
    protected UserRepository userRepository;

    // 公共方法：查询用户今日饮食记录（供子类复用或修改）
    protected List<FoodLog> findTodayFoodLogs(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return foodLogRepository.findByUserIdAndEatTimeBetween(userId, startOfDay, endOfDay);
    }

    // 公共方法：查询用户近N天饮食记录（供子类复用或修改）
    protected List<FoodLog> findRecentFoodLogs(Long userId, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("查询天数必须大于0");
        }
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        return foodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    public abstract List<FoodLog> getRecentFoodLogsByUser(Long userId, int recentDays);
}