package com.aihealthdiet.service.impl;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.repository.FoodLogRepository;
import com.aihealthdiet.repository.UserRepository;
import com.aihealthdiet.service.FoodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

    // ... 在你的类中，于其他业务方法旁插入以下方法 ...
    @Service
    public class FoodLogServiceImpl implements FoodLogService {

        @Autowired
        private FoodLogRepository foodLogRepository;

        @Override
        public FoodLog addFoodLog(Long userId, FoodLog foodLog) {
            // 设置用户ID（如果 FoodLog 没有 userId 字段，可能需要关联用户）
            foodLog.setUserId(userId); // 假设 FoodLog 有 setUserId 方法
            // 保存到数据库
            return foodLogRepository.save(foodLog);
        }

        @Autowired
        private UserRepository userRepository;

        // 查询方法使用只读事务（正确用法）
        @Transactional(readOnly = true)
        public List<FoodLog> getFoodLogsByUserId(Long userId) {
            return foodLogRepository.findByUserId(userId);
        }
        // ... 你原有的其他方法 ...

        /**
         * 根据用户ID获取其所有饮食记录
         * @param userId 用户ID
         * @return 该用户的饮食记录列表，按时间倒序排列
         */
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

        /**
         * 增强版：根据用户ID和最近天数获取饮食记录
         * @param userId 用户ID
         * @param recentDays 最近天数（例如7表示查询最近7天）
         * @return 最近N天的饮食记录
         */
        public List<FoodLog> getRecentFoodLogsByUser(Long userId, int recentDays) {
            if (recentDays <= 0) {
                throw new IllegalArgumentException("查询天数必须大于0");
            }
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(recentDays);
            return foodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        }
    }

