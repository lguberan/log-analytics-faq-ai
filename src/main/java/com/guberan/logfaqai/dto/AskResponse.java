package com.guberan.logfaqai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AskResponse {
    private String question;
    private String answer;
    private boolean validated;
    private List<String> snippets;
}