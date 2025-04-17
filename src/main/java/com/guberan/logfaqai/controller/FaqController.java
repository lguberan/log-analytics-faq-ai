package com.guberan.logfaqai.controller;

import com.guberan.logfaqai.dto.AskRequest;
import com.guberan.logfaqai.dto.AskResponse;
import com.guberan.logfaqai.service.FaqService;
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
    public ResponseEntity<AskResponse> askQuestion(@RequestBody AskRequest request) {
        AskResponse response = faqService.processQuestion(request.getQuestion());
        return ResponseEntity.ok(response);
    }
}