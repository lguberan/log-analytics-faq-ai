package com.guberan.logfaqai.dto;

import lombok.Data;

@Data
public class AskResponse {
    private String question;
    private String answer;
    private boolean validated;
}