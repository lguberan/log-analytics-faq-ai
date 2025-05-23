package com.guberan.logfaqai.service;

import com.guberan.logfaqai.dto.AskResponse;
import com.guberan.logfaqai.dto.LLMResponse;
import com.guberan.logfaqai.dto.ValidationRequest;
import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {

    private final OpenAiService openAiService;
    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;

    public AskResponse processQuestion(String questionText) {
        LLMResponse llmResponse = openAiService.generateAnswer(questionText);

        Question question = new Question(null, questionText, llmResponse.answer(), false, true, LocalDateTime.now());
        questionRepository.save(question);

        return new AskResponse(questionText, llmResponse.answer(), false, llmResponse.snippets());
    }

    public List<Question> getQuestionsByValidated(Boolean validated) {
        if (validated == null) {
            return questionRepository.findAll();
        }
        return questionRepository.findByValidated(validated);
    }

    public boolean validateResponse(Long id, ValidationRequest request) {
        Optional<Question> optional = questionRepository.findById(id);
        if (optional.isEmpty()) return false;

        Question question = optional.get();
        question.setAnswer(request.getCorrectedAnswer());
        question.setValidated(true);
        question.setAutoGenerated(false);
        questionRepository.save(question);

        log.info("🔄 Triggering RAG index rebuild...");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8001/rag/rebuild", null, String.class);
        log.info("✅ RAG rebuild request sent. response ={}", response);

        return true;
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}