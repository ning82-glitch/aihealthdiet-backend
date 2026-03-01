package com.aihealthdiet.service;

import com.aihealthdiet.model.FoodLog;
import com.aihealthdiet.repository.FoodLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodLogService {
    @Autowired
    private FoodLogRepository foodLogRepository;

    public FoodLog addFoodLog(FoodLog log) {
        return foodLogRepository.save(log);
    }

    public List<FoodLog> getFoodLogsByUser(Long userId) {
        return foodLogRepository.findByUserId(userId);
    }
}