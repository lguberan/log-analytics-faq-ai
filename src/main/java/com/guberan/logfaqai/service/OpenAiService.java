package com.guberan.logfaqai.service;

import com.guberan.logfaqai.config.OpenAiConfig;
import com.guberan.logfaqai.dto.LLMResponse;
import com.guberan.logfaqai.external.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    private final OpenAiConfig config;
    private final RestTemplate restTemplate;

    
    public LLMResponse generateAnswer(String question) {
        // call RAG microservice
        List<String> snippets = fetchRelevantSnippets(question);
        String context = snippets.isEmpty() ?
                "No relevant context found." :
                String.join("\n- ", snippets);

        String fullPrompt = "Answer the following question using the context below:\n"
                + "- " + context + "\n\nQuestion: " + question;

        log.info("🧠 Generated prompt for OpenAI:\n" + fullPrompt);
        log.info("📎 Snippets used: " + snippets);

        Map<String, Object> message = Map.of("role", "user", "content", fullPrompt);
        Map<String, Object> requestBody = Map.of(
                "model", config.getModel(),
                "messages", List.of(message)
        );

        String answer = webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(r -> r.getChoices().get(0).getMessage().getContent())
                .block();
        return new LLMResponse(answer, snippets);
    }

    private List<String> fetchRelevantSnippets(String query) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = "{\"question\": \"" + query.replace("\"", "\\\"") + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://localhost:8001/rag/query", entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body != null && body.containsKey("snippets")) {
                    return (List<String>) body.get("snippets");
                }
            }
        } catch (Exception e) {
            log.error("⚠️ RAG query failed: " + e.getMessage());
        }
        return List.of();
    }
}