package com.aihealthdiet.repository;

import com.aihealthdiet.model.HealthGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HealthGoalRepository extends JpaRepository<HealthGoal, Long> {
    List<HealthGoal> findByUserId(Long userId);
}