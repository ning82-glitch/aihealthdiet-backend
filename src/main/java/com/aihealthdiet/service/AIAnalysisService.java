package com.aihealthdiet.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIAnalysisService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeFoodImage(String imageUrl) {
        // 这里调用外部AI微服务
        String aiServiceUrl = "http://localhost:5000/analyze?imageUrl=" + imageUrl;
        return restTemplate.getForObject(aiServiceUrl, String.class);
    }
}