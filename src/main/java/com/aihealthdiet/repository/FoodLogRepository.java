package com.aihealthdiet.repository;

import com.aihealthdiet.entity.FoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodLogRepository extends JpaRepository<FoodLog, Long> {

    // 1. 使用标准命名规则 - 这个没问题
    List<FoodLog> findByUser_Id(Long userId);

    List<FoodLog> findByUser_IdAndEatTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // 2. 查询用户某一天的饮食记录 - 修复：DATE(f.eatTime)
    @Query("SELECT f FROM FoodLog f WHERE f.user.id = :userId AND DATE(f.eatTime) = :date")
    List<FoodLog> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 3. 查询用户最近7天的饮食记录 - 修复：f.eatTime >= :startDate
    @Query("SELECT f FROM FoodLog f WHERE f.user.id = :userId AND f.eatTime >= :startDate")
    List<FoodLog> findRecentByUserId(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    // 4. 统计用户某天的总热量 - 修复：SUM(f.calories), DATE(f.eatTime)
    @Query("SELECT COALESCE(SUM(f.calories), 0) FROM FoodLog f WHERE f.user.id = :userId AND DATE(f.eatTime) = :date")
    Double sumCaloriesByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 5. 根据用户ID和日期范围查询饮食记录 - 修复：f.eatTime BETWEEN
    @Query("SELECT f FROM FoodLog f WHERE f.user.id = :userId AND f.eatTime BETWEEN :startDate AND :endDate ORDER BY f.eatTime DESC")
    List<FoodLog> findByUserIdAndDateRange(@Param("userId") Long userId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);



    // 7. 可选：添加按餐类型查询
    List<FoodLog> findByUser_IdAndMealType(Long userId, String mealType);

    // 8. 可选：查询最近N条记录
    List<FoodLog> findTop10ByUser_IdOrderByEatTimeDesc(Long userId);
}