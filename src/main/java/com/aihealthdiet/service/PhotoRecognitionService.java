package com.aihealthdiet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PhotoRecognitionService {

    @Value("${ai.vision.api.key:}")
    private String apiKey;

    @Value("${ai.vision.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 识别食物照片（简化版，实际需要集成真正的视觉API）
     */
    public Map<String, Object> recognizeFood(MultipartFile imageFile) {
        // 这里应该调用真正的视觉识别API，如Google Vision, OpenAI CLIP等
        // 由于API密钥和成本问题，这里返回模拟数据

        Map<String, Object> result = new HashMap<>();
        result.put("fileName", imageFile.getOriginalFilename());
        result.put("fileSize", imageFile.getSize());

        // 模拟识别结果
        String[] possibleFoods = {
                "苹果", "香蕉", "鸡胸肉", "米饭", "面条",
                "蔬菜沙拉", "三明治", "披萨", "汉堡", "牛排"
        };

        String recognizedFood = possibleFoods[new Random().nextInt(possibleFoods.length)];

        // 食物营养数据库
        Map<String, Map<String, Double>> nutritionDB = new HashMap<>();
        nutritionDB.put("苹果", Map.of("calories", 52.0, "protein", 0.3, "carbs", 14.0, "fat", 0.2));
        nutritionDB.put("香蕉", Map.of("calories", 89.0, "protein", 1.1, "carbs", 23.0, "fat", 0.3));
        nutritionDB.put("鸡胸肉", Map.of("calories", 165.0, "protein", 31.0, "carbs", 0.0, "fat", 3.6));
        nutritionDB.put("米饭", Map.of("calories", 130.0, "protein", 2.7, "carbs", 28.0, "fat", 0.3));
        nutritionDB.put("面条", Map.of("calories", 138.0, "protein", 4.5, "carbs", 25.0, "fat", 2.1));

        Map<String, Double> nutrition = nutritionDB.getOrDefault(recognizedFood,
                Map.of("calories", 200.0, "protein", 10.0, "carbs", 20.0, "fat", 8.0));

        result.put("recognizedFood", recognizedFood);
        result.put("confidence", 0.85);
        result.put("calories", nutrition.get("calories"));
        result.put("protein", nutrition.get("protein"));
        result.put("carbs", nutrition.get("carbs"));
        result.put("fat", nutrition.get("fat"));
        result.put("suggestedPortion", "1份");
        result.put("note", "此数据为估算值，实际营养可能因烹饪方式和分量有所不同");

        return result;
    }

    /**
     * 如果集成真正的OpenAI Vision API
     */
    public Map<String, Object> recognizeFoodWithOpenAI(MultipartFile imageFile) throws IOException {
        if (apiKey.isEmpty()) {
            throw new RuntimeException("未配置AI视觉API密钥");
        }

        // 将图片转换为base64
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());

        // 构建请求
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4-vision-preview");

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");

        List<Map<String, Object>> content = new ArrayList<>();
        content.add(Map.of("type", "text", "text", "识别这张图片中的食物，并估算其营养成分（卡路里、蛋白质、碳水化合物、脂肪）。"));
        content.add(Map.of(
                "type", "image_url",
                "image_url", Map.of("url", "data:image/jpeg;base64," + base64Image)
        ));

        message.put("content", content);
        messages.add(message);

        request.put("messages", messages);
        request.put("max_tokens", 300);

        // 调用API（需要处理HTTP请求）
        // 这里简化处理，实际需要发送HTTP请求

        return Map.of(
                "status", "需要集成真正的OpenAI API",
                "apiKeyConfigured", !apiKey.isEmpty()
        );
    }
}