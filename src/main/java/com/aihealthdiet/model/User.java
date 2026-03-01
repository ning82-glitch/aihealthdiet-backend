package com.aihealthdiet.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private int age;
    private double height;
    private double weight;
    private String gender;
    private String medicalHistory;
    private String allergyInfo;
}