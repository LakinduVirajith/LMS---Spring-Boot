package com.vpcodelabs.lms.dtos;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    private String errorCode;
    
    private String timestamp;

    @Builder.Default
    private Map<String, String> validationErrors = new HashMap<>();
}