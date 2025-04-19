package com.guberan.logfaqai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guberan.logfaqai.config.SecurityConfig;
import com.guberan.logfaqai.dto.ValidationRequest;
import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.service.FaqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FaqService faqService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ADMIN", "RAG_BUILDER"})
    void shouldReturnQuestions() throws Exception {
        Question q = new Question(1L, "What is Kafka?", "Kafka is a messaging system.", true, true, LocalDateTime.now());
        when(faqService.getQuestionsByValidated(true)).thenReturn(List.of(q));

        mockMvc.perform(get("/api/admin/questions?validated=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("What is Kafka?"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldValidateAndUpdateAnswer() throws Exception {
        ValidationRequest request = new ValidationRequest();
        request.setCorrectedAnswer("Kafka streams logs in real time.");
        when(faqService.validateResponse(eq(1L), any(ValidationRequest.class))).thenReturn(true);

        mockMvc.perform(post("/api/admin/questions/1/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Question validated."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteQuestion() throws Exception {
        doNothing().when(faqService).deleteQuestion(1L);

        mockMvc.perform(delete("/api/admin/questions/1"))
                .andExpect(status().isOk());

        verify(faqService).deleteQuestion(1L);
    }

    @Test
    @WithMockUser
    void shouldRejectAccessForNonAdminUser() throws Exception {
        mockMvc.perform(post("/api/admin/questions/{id}/validate", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"correctedAnswer\": \"Test Answer\" }"))
                .andExpect(status().isForbidden());
    }
}