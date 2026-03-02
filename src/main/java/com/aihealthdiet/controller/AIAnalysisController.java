package com.aihealthdiet.controller;

import com.aihealthdiet.service.AIAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIAnalysisController {

    @Autowired
    private AIAnalysisService aiAnalysisService;

    @GetMapping("/analyze-habits")
    public ResponseEntity<Map<String, Object>> analyzeEatingHabits(@RequestParam Long userId) {
        Map<String, Object> analysis = aiAnalysisService.analyzeEatingHabits(userId);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/recommend-recipes")
    public ResponseEntity<Map<String, Object>> recommendRecipes(@RequestParam Long userId) {
        Map<String, Object> recommendations = aiAnalysisService.generateRecipeRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/search-inventory")
    public ResponseEntity<Map<String, Object>> searchInventory(@RequestParam String ingredient) {
        Map<String, Object> inventory = aiAnalysisService.searchSupermarketInventory(ingredient);
        return ResponseEntity.ok(inventory);
    }
}