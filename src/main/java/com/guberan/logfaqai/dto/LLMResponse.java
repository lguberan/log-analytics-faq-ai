package com.guberan.logfaqai.dto;

import java.util.List;

public record LLMResponse(String answer, List<String> snippets) {
}