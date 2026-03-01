package com.aihealthdiet.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class HealthGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String goalType; // 如减脂、增肌、控糖等
    private String goalDetail;
    private boolean achieved;
}