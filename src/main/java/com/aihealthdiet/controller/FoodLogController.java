package com.aihealthdiet.controller;

import com.aihealthdiet.entity.FoodLog;
import com.aihealthdiet.service.FoodLogService;
import com.aihealthdiet.service.PhotoRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foodlog")
public class FoodLogController {

    @Autowired
    private FoodLogService foodLogService;

    @Autowired
    private PhotoRecognitionService photoRecognitionService;

    @GetMapping("/list")
    public ResponseEntity<List<FoodLog>> listFoodLogs(@RequestParam Long userId) {
        List<FoodLog> logs = foodLogService.getFoodLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/today")
    public ResponseEntity<List<FoodLog>> todayFoodLogs(@RequestParam Long userId) {
        List<FoodLog> logs = foodLogService.getTodayFoodLogs(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<FoodLog>> recentFoodLogs(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "7") int days) {
        List<FoodLog> logs = foodLogService.getRecentFoodLogs(userId, days);
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/add")
    public ResponseEntity<FoodLog> addFoodLog(
            @RequestParam Long userId,
            @RequestBody FoodLog foodLog) {
        FoodLog savedLog = foodLogService.addFoodLog(userId, foodLog);
        return ResponseEntity.ok(savedLog);
    }

    @DeleteMapping("/delete/{logId}")
    public ResponseEntity<Map<String, String>> deleteFoodLog(
            @PathVariable Long logId,
            @RequestParam Long userId) {
        try {
            foodLogService.deleteFoodLog(logId, userId);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/nutrition/today")
    public ResponseEntity<Map<String, Double>> todayNutrition(@RequestParam Long userId) {
        Map<String, Double> nutrition = foodLogService.getTodayNutritionSummary(userId);
        return ResponseEntity.ok(nutrition);
    }

    @GetMapping("/meal-distribution")
    public ResponseEntity<Map<String, Long>> mealDistribution(@RequestParam Long userId) {
        Map<String, Long> distribution = foodLogService.getMealTypeDistribution(userId);
        return ResponseEntity.ok(distribution);
    }

    @PostMapping("/recognize-photo")
    public ResponseEntity<Map<String, Object>> recognizeFoodPhoto(
            @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = photoRecognitionService.recognizeFood(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "识别失败: " + e.getMessage()
            ));
        }
    }
}