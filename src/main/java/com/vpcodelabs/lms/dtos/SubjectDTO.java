package com.vpcodelabs.lms.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectDTO {
    @NotNull(message = "cannot be null")
    @Size(min = 100, message = "Subject must be at least 100 characters long")
    private String subjectName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String courseImageUrl;

    @NotNull
    private Long mentorId;
}