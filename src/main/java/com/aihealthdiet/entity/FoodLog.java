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


    @Column(nullable = false)
    private String foodName;

    @Column(length = 1000)
    private String description;

    private String nutritionResult;


    private Double calories; // 卡路里
    private Double protein;  // 蛋白质(g)
    private Double carbs;    // 碳水化合物(g)
    private Double fat;      // 脂肪(g)

    @Column(name = "eat_time")
    private LocalDateTime eatTime = LocalDateTime.now();

    @Column(name = "meal_type")
    private String mealType; // 餐次: breakfast, lunch, dinner, snack

    @Column(name = "input_method")
    private String inputMethod; // 输入方式: manual, photo, voice

    @Column(name = "photo_url")
    private String photoUrl; // 食物照片URL

    @Column(name = "food_image_url", length = 500)
    private String foodImageUrl; // 食物图片的URL地址

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}