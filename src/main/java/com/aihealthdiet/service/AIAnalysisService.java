package com.aihealthdiet.service;

import com.aihealthdiet.entity.FoodLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIAnalysisService {

    public String analyzeFoodImage(String foodImageUrl) {
        // TODO: 这里替换为真实的AI分析逻辑（调用API、模型推理等）
        // 暂时模拟返回一个结果
        return "食物分析结果：热量250kcal，蛋白质15g，脂肪8g，碳水30g";
    }

    @Autowired
    private FoodLogService foodLogService;

    @Autowired
    private UserService userService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 分析用户近期的饮食习惯
     */
    public Map<String, Object> analyzeEatingHabits(Long userId) {
        List<FoodLog> recentLogs = foodLogService.getRecentFoodLogs(userId, 7);

        if (recentLogs.isEmpty()) {
            return Map.of("message", "近期无饮食记录", "suggestions", Collections.emptyList());
        }

        // 分析各种营养素的平均摄入
        double avgCalories = recentLogs.stream()
                .mapToDouble(f -> f.getCalories() != null ? f.getCalories() : 0)
                .average()
                .orElse(0);

        double avgProtein = recentLogs.stream()
                .mapToDouble(f -> f.getProtein() != null ? f.getProtein() : 0)
                .average()
                .orElse(0);

        double avgCarbs = recentLogs.stream()
                .mapToDouble(f -> f.getCarbs() != null ? f.getCarbs() : 0)
                .average()
                .orElse(0);

        double avgFat = recentLogs.stream()
                .mapToDouble(f -> f.getFat() != null ? f.getFat() : 0)
                .average()
                .orElse(0);

        // 分析餐次分布
        Map<String, Long> mealDistribution = recentLogs.stream()
                .collect(Collectors.groupingBy(
                        FoodLog::getMealType,
                        Collectors.counting()
                ));

        // 生成建议
        List<String> suggestions = new ArrayList<>();

        if (avgCalories > 2500) {
            suggestions.add("近期平均热量摄入较高，建议适量控制饮食");
        } else if (avgCalories < 1200) {
            suggestions.add("近期热量摄入不足，可能影响基础代谢");
        }

        if (avgProtein < 50) {
            suggestions.add("蛋白质摄入偏低，建议增加鸡蛋、鸡胸肉、豆制品等");
        }

        if (avgFat > 80) {
            suggestions.add("脂肪摄入偏高，建议减少油炸食品和高脂肪食物");
        }

        if (!mealDistribution.containsKey("breakfast") || mealDistribution.get("breakfast") < 3) {
            suggestions.add("早餐记录较少，规律早餐有助于控制全天食欲");
        }

        if (mealDistribution.getOrDefault("snack", 0L) > 5) {
            suggestions.add("零食记录较多，建议控制零食摄入时间和频率");
        }

        return Map.of(
                "analysisPeriod", "7天",
                "totalMeals", recentLogs.size(),
                "avgCalories", String.format("%.1f", avgCalories),
                "avgProtein", String.format("%.1f", avgProtein),
                "avgCarbs", String.format("%.1f", avgCarbs),
                "avgFat", String.format("%.1f", avgFat),
                "mealDistribution", mealDistribution,
                "suggestions", suggestions
        );
    }

    /**
     * 生成个性化食谱建议
     */
    public Map<String, Object> generateRecipeRecommendations(Long userId) {
        Optional<com.aihealthdiet.entity.User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Map.of("error", "用户不存在");
        }

        com.aihealthdiet.entity.User user = userOpt.get();
        Map<String, Double> todayNutrition = foodLogService.getTodayNutritionSummary(userId);
        double todayCalories = todayNutrition.getOrDefault("calories", 0.0);

        // 获取用户目标
        Integer dailyCalorieGoal = user.getDailyCalorieGoal();
        if (dailyCalorieGoal == null || dailyCalorieGoal == 0) {
            dailyCalorieGoal = 2000; // 默认值
        }

        double remainingCalories = dailyCalorieGoal - todayCalories;

        // 根据过敏源过滤食谱
        String allergies = user.getAllergies();
        Set<String> allergySet = allergies != null ?
                Arrays.stream(allergies.split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        // 食谱数据库（简化版）
        List<Map<String, Object>> recipes = new ArrayList<>();

        // 早餐食谱
        recipes.add(Map.of(
                "name", "全麦三明治",
                "type", "breakfast",
                "calories", 350,
                "protein", 15,
                "carbs", 45,
                "fat", 12,
                "ingredients", List.of("全麦面包", "鸡蛋", "生菜", "番茄"),
                "description", "高蛋白低GI早餐，提供持久能量",
                "allergens", List.of("鸡蛋", "小麦")
        ));

        recipes.add(Map.of(
                "name", "燕麦牛奶粥",
                "type", "breakfast",
                "calories", 280,
                "protein", 12,
                "carbs", 40,
                "fat", 8,
                "ingredients", List.of("燕麦", "低脂牛奶", "蓝莓", "杏仁"),
                "description", "富含膳食纤维，有益肠道健康",
                "allergens", List.of("牛奶", "坚果")
        ));

        // 午餐食谱
        recipes.add(Map.of(
                "name", "香煎鸡胸肉沙拉",
                "type", "lunch",
                "calories", 450,
                "protein", 35,
                "carbs", 20,
                "fat", 18,
                "ingredients", List.of("鸡胸肉", "混合蔬菜", "橄榄油", "柠檬汁"),
                "description", "高蛋白低脂，适合减脂期",
                "allergens", Collections.emptyList()
        ));

        // 晚餐食谱
        recipes.add(Map.of(
                "name", "清蒸鲈鱼配蔬菜",
                "type", "dinner",
                "calories", 380,
                "protein", 30,
                "carbs", 15,
                "fat", 20,
                "ingredients", List.of("鲈鱼", "西兰花", "胡萝卜", "姜"),
                "description", "清淡易消化，适合晚餐",
                "allergens", List.of("鱼")
        ));

        // 根据过敏源过滤
        List<Map<String, Object>> filteredRecipes = recipes.stream()
                .filter(recipe -> {
                    List<String> allergens = (List<String>) recipe.get("allergens");
                    return allergens.stream().noneMatch(allergySet::contains);
                })
                .collect(Collectors.toList());

        // 根据剩余热量推荐
        List<Map<String, Object>> recommended = filteredRecipes.stream()
                .filter(recipe -> (Double) recipe.get("calories") <= remainingCalories + 100)
                .sorted(Comparator.comparingDouble(r -> Math.abs((Double) r.get("calories") - remainingCalories / 2)))
                .limit(3)
                .collect(Collectors.toList());

        return Map.of(
                "todayCalories", todayCalories,
                "dailyCalorieGoal", dailyCalorieGoal,
                "remainingCalories", remainingCalories,
                "recommendedRecipes", recommended,
                "allergiesConsidered", allergySet
        );
    }

    /**
     * 对接超市库存（模拟第三方API调用）
     */
    public Map<String, Object> searchSupermarketInventory(String ingredient) {
        // 模拟调用超市API
        String[] supermarkets = {"美团买菜", "叮咚买菜", "盒马鲜生", "京东到家"};
        Random random = new Random();

        List<Map<String, Object>> results = new ArrayList<>();
        for (String supermarket : supermarkets) {
            results.add(Map.of(
                    "supermarket", supermarket,
                    "ingredient", ingredient,
                    "price", String.format("%.2f", 5 + random.nextDouble() * 20),
                    "unit", "500g",
                    "inStock", random.nextBoolean(),
                    "deliveryTime", random.nextInt(2) + "小时"
            ));
        }

        return Map.of(
                "searchQuery", ingredient,
                "results", results
        );
    }

    // 需要注入UserRepository
    @Autowired
    private com.aihealthdiet.repository.UserRepository userRepository;
}
