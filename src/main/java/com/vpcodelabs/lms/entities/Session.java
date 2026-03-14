package com.vpcodelabs.lms.entities;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpcodelabs.lms.constants.PaymentStatus;
import com.vpcodelabs.lms.constants.SessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "session")
@Data
@Builder
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_at", nullable = false)
    private Date sessionAt;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "session_status")
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Column(name = "session_notes", columnDefinition = "TEXT")
    private String sessionNotes;

    @Column(name = "student_review", columnDefinition = "TEXT")
    private String studentReview;

    @Column(name = "student_rating")
    private Integer studentRating;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    @JsonIgnore
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnore
    private Subject subject;
}
