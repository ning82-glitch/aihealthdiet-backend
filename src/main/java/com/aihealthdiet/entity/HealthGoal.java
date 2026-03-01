package com.aihealthdiet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "health_goals")
@Data
public class HealthGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String goalType; // 目标类型: weight_loss, muscle_gain, maintain

    @Column(name = "start_date")
    private LocalDate startDate = LocalDate.now();

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "initial_weight")
    private Double initialWeight;

    @Column(name = "target_weight")
    private Double targetWeight;

    @Column(name = "weekly_target")
    private Double weeklyTarget; // 每周目标(kg)

    @Column(length = 1000)
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}