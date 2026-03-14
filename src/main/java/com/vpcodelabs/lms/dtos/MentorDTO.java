package com.vpcodelabs.lms.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MentorDTO {
    private String mentorId;

    @Size(min = 3, message = "First name must be at least 3 characters long")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(min = 3, message = "Last name must be at least 3 characters long")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 7, message = "Phone number must be at least 7 characters")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phoneNumber;

    @Size(min = 3, message = "Title must be at least 3 characters long")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(min = 3, message = "Profession must be at least 3 characters long")
    @Size(max = 100, message = "Profession must not exceed 100 characters")
    private String profession;

    @Size(min = 3, message = "Company must be at least 3 characters long")
    @Size(max = 100, message = "Company must not exceed 100 characters")
    private String company;


    private Integer experienceYears;

    @Size(min = 10, message = "Bio must be at least 10 characters long")
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    private String profileImageUrl;

    private Integer positiveReviews;

    private Integer totalEnrollments;

    private Boolean isCertified;

    private Integer startYear;
}
