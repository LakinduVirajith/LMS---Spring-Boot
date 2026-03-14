package com.vpcodelabs.lms.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionReviewDTO {
    private Long sessionId;
    
    @NotBlank(message = "Review cannot be empty")
    private String studentReview;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer studentRating;
}
