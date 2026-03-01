package com.aihealthdiet.controller;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.service.AIAnalysisService;
import com.aihealthdiet.service.FoodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodlog")
public class FoodLogController {
    @Autowired
    private FoodLogService foodLogService;
    @Autowired
    private AIAnalysisService aiAnalysisService;

    @PostMapping("/add")
    public FoodLog addFoodLog(@RequestBody FoodLog log) {
        String analysisResult = aiAnalysisService.analyzeFoodImage(log.getFoodImageUrl());
        log.setNutritionResult(analysisResult);
        return foodLogService.addFoodLog(log);
    }

    @GetMapping("/user/{userId}")
    public List<FoodLog> getFoodLogs(@PathVariable Long userId) {
        return foodLogService.getFoodLogsByUser(userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoodLog>> getFoodLogsByUser(@PathVariable Long userId) {
        try {
            List<FoodLog> logs = foodLogService.getFoodLogsByUser(userId);
            return ResponseEntity.ok(logs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}