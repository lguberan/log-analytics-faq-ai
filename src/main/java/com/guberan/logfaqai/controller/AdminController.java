package com.guberan.logfaqai.controller;

import com.guberan.logfaqai.dto.ValidationRequest;
import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('SCOPE_admin')")
public class AdminController {

    private final FaqService faqService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RAG_BUILDER')")
    public List<Question> getQuestions(@RequestParam(required = false) Boolean validated) {
        return faqService.getQuestionsByValidated(validated);
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Validate or correct a generated answer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Manual correction",
                                            summary = "Validate with a corrected answer",
                                            value = """
                                                        {
                                                          "correctedAnswer": "Kafka processes real-time logs and sends them to Elasticsearch."
                                                        }
                                                    """
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<String> validateResponse(@PathVariable Long id, @RequestBody ValidationRequest request) {
        return faqService.validateResponse(id, request)
                ? ResponseEntity.ok("Question validated.")
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        faqService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }
}