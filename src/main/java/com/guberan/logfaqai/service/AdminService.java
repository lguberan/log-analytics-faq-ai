package com.guberan.logfaqai.service;

import com.guberan.logfaqai.dto.AskResponse;
import com.guberan.logfaqai.dto.LLMResponse;
import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OpenAiService openAiService;
    private final QuestionRepository questionRepository;

    public AskResponse processQuestion(String questionText) {
        LLMResponse llmResponse = openAiService.generateAnswer(questionText);

        Question question = new Question(null, questionText, llmResponse.answer(), false, true, LocalDateTime.now());
        questionRepository.save(question);

        return new AskResponse(questionText, llmResponse.answer(), false, llmResponse.snippets());
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}