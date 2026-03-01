package com.aihealthdiet.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    private Double height; // 身高(cm)
    private Double weight; // 体重(kg)

    @Column(name = "target_weight")
    private Double targetWeight; // 目标体重(kg)

    @Column(name = "daily_calorie_goal")
    private Integer dailyCalorieGoal; // 每日卡路里目标

    @Column(name = "allergies")
    private String allergies; // 过敏源，用逗号分隔，如"花生,海鲜"

    @Column(name = "preferred_cuisine")
    private String preferredCuisine; // 饮食偏好，如"中餐,日料"

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FoodLog> foodLogs = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<HealthGoal> healthGoals = new HashSet<>();
}