package com.guberan.logfaqai.controller;

import com.guberan.logfaqai.dto.AskRequest;
import com.guberan.logfaqai.dto.AskResponse;
import com.guberan.logfaqai.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @PostMapping("/ask")
    @Operation(
            summary = "Ask a question about the log analytics platform",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Kafka Example",
                                            summary = "Question about Kafka",
                                            value = """
                                                        {
                                                          "question": "How is Kafka used in this project?"
                                                        }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "OpenAI Example",
                                            summary = "Question about OpenAI usage",
                                            value = """
                                                        {
                                                          "question": "How does OpenAI integrate with the FAQ system?"
                                                        }
                                                    """
                                    )
                            }

                    )
            )
    )
    public ResponseEntity<AskResponse> askQuestion(@RequestBody AskRequest request) {
        AskResponse response = faqService.processQuestion(request.getQuestion());
        return ResponseEntity.ok(response);
    }
}