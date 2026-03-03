package com.aihealthdiet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_logs")
@Data
public class FoodLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 修正：指定列名 food_name
    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(length = 1000)
    private String description;

    // ✅ 修正：指定列名 nutrition_result
    @Column(name = "nutrition_result")
    private String nutritionResult;

    // ✅ 修正：指定列名（虽然同名，但明确指定更安全）
    @Column(name = "calories")
    private Double calories;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "fat")
    private Double fat;

    @Column(name = "eat_time")
    private LocalDateTime eatTime;

    @Column(name = "meal_type")
    private String mealType;

    @Column(name = "input_method")
    private String inputMethod;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "food_image_url", length = 500)
    private String foodImageUrl;

    // ✅ 添加：created_at 字段
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ 添加：自动设置创建时间
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (eatTime == null) {
            eatTime = LocalDateTime.now();
        }
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}