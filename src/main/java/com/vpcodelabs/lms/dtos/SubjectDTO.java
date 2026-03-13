package com.vpcodelabs.lms.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectDTO {
    @NotNull(message = "Subject name cannot be null")
    @Size(min = 5, message = "Subject must be at least 5 characters long")
    @Size(max = 100, message = "Subject must not exceed 100 characters")
    private String subjectName;

    @Size(min = 10, message = "Description must be at least 10 characters long")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String courseImageUrl;

    @NotNull(message = "Mentor ID cannot be null")
    private Long mentorId;
}