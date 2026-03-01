package com.aihealthdiet.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class FoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String foodImageUrl;
    private String foodDescription;
    private LocalDateTime timestamp;
    private String nutritionResult; // AI分析结果
}