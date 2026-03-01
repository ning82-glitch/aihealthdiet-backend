package com.aihealthdiet.repository;

import com.aihealthdiet.entity.HealthGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthGoalRepository extends JpaRepository<HealthGoal, Long> {
    List<HealthGoal> findByUserId(Long userId);
    Optional<HealthGoal> findByUserIdAndGoalType(Long userId, String goalType);
}