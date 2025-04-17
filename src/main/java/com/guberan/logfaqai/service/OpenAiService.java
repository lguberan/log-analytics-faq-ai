package com.guberan.logfaqai.service;

import com.guberan.logfaqai.config.OpenAiConfig;
import com.guberan.logfaqai.external.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private final OpenAiConfig config;

    public String getAnswer(String question) {
        Map<String, Object> message = Map.of("role", "user", "content", question);
        Map<String, Object> requestBody = Map.of(
                "model", config.getModel(),
                "messages", List.of(message)
        );

        return webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(r -> r.getChoices().get(0).getMessage().getContent())
                .block();
    }
}