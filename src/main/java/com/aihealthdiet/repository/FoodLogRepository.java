package com.aihealthdiet.repository;

import com.aihealthdiet.model.FoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodLogRepository extends JpaRepository<FoodLog, Long> {
    List<FoodLog> findByUserId(Long userId);
}