package com.guberan.logfaqai.service;

import com.guberan.logfaqai.dto.AskResponse;
import com.guberan.logfaqai.dto.LLMResponse;
import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FaqServiceTest {

    @Mock
    private OpenAiService openAiService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FaqService faqService;

    @Test
    void shouldGenerateAndStoreQuestion() {
        String questionText = "How does Kafka work in this project?";
        LLMResponse llmResponse = new LLMResponse("Kafka is used for real-time log ingestion.", List.of());

        // simulate GPT response
        Mockito.when(openAiService.generateAnswer(questionText)).thenReturn(llmResponse);

        // when
        AskResponse response = faqService.processQuestion(questionText);

        // then
        assertEquals(questionText, response.getQuestion());
        assertEquals(llmResponse.answer(), response.getAnswer());
        assertFalse(response.isValidated());

        // verify save() called with expected object
        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(captor.capture());

        Question saved = captor.getValue();
        assertEquals(questionText, saved.getText());
        assertEquals(llmResponse.answer(), saved.getAnswer());
        assertFalse(saved.isValidated());
        assertTrue(saved.isAutoGenerated());
        assertNotNull(saved.getAskedAt());
    }
}