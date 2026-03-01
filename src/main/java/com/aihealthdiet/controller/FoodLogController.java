package com.aihealthdiet.controller;

import com.aihealthdiet.model.FoodLog;
import com.aihealthdiet.service.FoodLogService;
import com.aihealthdiet.service.AIAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
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
}