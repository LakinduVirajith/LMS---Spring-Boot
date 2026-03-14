package com.vpcodelabs.lms.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectResponseDTO {

    private Long id;

    private String subjectName;

    private String description;

    private String courseImageUrl;

    private Long mentorId;

    private String mentorName;
}
