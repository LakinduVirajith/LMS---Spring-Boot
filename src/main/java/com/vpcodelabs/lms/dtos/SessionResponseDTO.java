package com.vpcodelabs.lms.dtos;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionResponseDTO {
    private Long id;

    private String studentName;

    private String mentorName;
    
    private String mentorProfileImageUrl;
    
    private String subjectName;
    
    private Date sessionAt;
    
    private Integer durationMinutes;
    
    private String sessionStatus;
    
    private String paymentStatus;
    
    private String meetingLink;
}
