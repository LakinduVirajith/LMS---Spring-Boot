package com.vpcodelabs.lms.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vpcodelabs.lms.constants.PaymentStatus;
import com.vpcodelabs.lms.constants.SessionStatus;
import com.vpcodelabs.lms.dtos.SessionDTO;
import com.vpcodelabs.lms.dtos.SessionResponseDTO;
import com.vpcodelabs.lms.dtos.SessionReviewDTO;
import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.entities.Session;
import com.vpcodelabs.lms.entities.Student;
import com.vpcodelabs.lms.entities.Subject;
import com.vpcodelabs.lms.exceptions.CustomException;
import com.vpcodelabs.lms.repositories.MentorRepository;
import com.vpcodelabs.lms.repositories.SessionRepository;
import com.vpcodelabs.lms.repositories.StudentRepository;
import com.vpcodelabs.lms.repositories.SubjectRepository;
import com.vpcodelabs.lms.security.UserPrincipal;
import com.vpcodelabs.lms.services.SessionService;
import com.vpcodelabs.lms.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    @Override
    public Session createNewSession(SessionDTO sessionDTO) {
        // Fetch the Related Entities by Their IDs
        try {
            Student student = studentRepository.findById(sessionDTO.getStudentId()).orElseThrow(
                    () -> new CustomException("Student not found", HttpStatus.NOT_FOUND)
            );
            Mentor mentor = mentorRepository.findById(sessionDTO.getMentorId()).orElseThrow(
                    () -> new CustomException("Mentor not found", HttpStatus.NOT_FOUND)
            );
            Subject subject = subjectRepository.findById(sessionDTO.getSubjectId()).orElseThrow(
                    () -> new CustomException("Subject not found", HttpStatus.NOT_FOUND)
            );

            ValidationUtils.validateMentorAvailability(mentor, sessionDTO.getSessionAt(), sessionDTO.getDurationMinutes());
            ValidationUtils.validateStudentAvailability(student, sessionDTO.getSessionAt(), sessionDTO.getDurationMinutes());

            Session session = modelMapper.map(sessionDTO, Session.class);
            session.setStudent(student);
            session.setMentor(mentor);
            session.setSubject(subject);

            return sessionRepository.save(session);
        } catch (CustomException exception) {
            log.error("Dependencies not found to map: {}, Failed to create new session", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Failed to create session", exception);
            throw new CustomException("Failed to create new session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<SessionResponseDTO> getAllSessions(Pageable pageable) {
        try {
            Page<Session> sessions = sessionRepository.findAll(pageable);

            return sessions.map(session ->
                SessionResponseDTO.builder()
                    .id(session.getId())
                    .studentName(
                            session.getStudent().getFirstName() + " " +
                            session.getStudent().getLastName()
                    )
                    .mentorName(
                            session.getMentor().getFirstName() + " " +
                            session.getMentor().getLastName()
                    )
                    .subjectName(session.getSubject().getSubjectName())
                    .sessionAt(session.getSessionAt())
                    .durationMinutes(session.getDurationMinutes())
                    .paymentStatus(session.getPaymentStatus().name())
                    .sessionStatus(session.getSessionStatus().name())
                    .meetingLink(session.getMeetingLink())
                    .build()
            );
        } catch (Exception exception) {
            throw new CustomException("Failed to get all sessions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Session getSessionById(Long id) {
        try {
            Session session = sessionRepository.findById(id).orElseThrow(
                    () -> new CustomException("Session Not found", HttpStatus.NOT_FOUND)
            );
            log.info("Successfully fetched session {}", id);
            return session;
        } catch (CustomException exception) {
            log.warn("Session not found with id: {} to fetch", id, exception);
            throw new CustomException("Session Not found", HttpStatus.NOT_FOUND);
        }catch (Exception exception) {
            log.error("Failed to get session by ID", exception);
            throw new CustomException("Failed to get session by ID", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Session updateSessionById(Long id, SessionDTO updatedSessionDTO) {
        try {
            Session session = sessionRepository.findById(id).orElseThrow(
                () -> new CustomException("Session Not found", HttpStatus.NOT_FOUND)
            );

            // Source -> Destination
            modelMapper.map(updatedSessionDTO, session);

            // Update the related entities
            if (updatedSessionDTO.getStudentId() != null) {
                Student student = studentRepository.findById(updatedSessionDTO.getStudentId()).orElseThrow(
                        () -> new CustomException("Student not found", HttpStatus.NOT_FOUND)
                );
                ValidationUtils.validateStudentAvailability(student, updatedSessionDTO.getSessionAt(), updatedSessionDTO.getDurationMinutes());
                session.setStudent(student);
            }
            if (updatedSessionDTO.getMentorId() != null) {
                Mentor mentor = mentorRepository.findById(updatedSessionDTO.getMentorId()).orElseThrow(
                        () -> new CustomException("Mentor not found", HttpStatus.NOT_FOUND)
                );
                ValidationUtils.validateMentorAvailability(mentor, updatedSessionDTO.getSessionAt(), updatedSessionDTO.getDurationMinutes());
                session.setMentor(mentor);
            }
            if (updatedSessionDTO.getSubjectId() != null) {
                Subject subject = subjectRepository.findById(updatedSessionDTO.getSubjectId()).orElseThrow(
                        () -> new CustomException("Subject not found", HttpStatus.NOT_FOUND)
                );
                session.setSubject(subject);
            }

            return sessionRepository.save(session);
        } catch (CustomException exception) {
            log.warn("Session not found with id: {} to update", id, exception);
            throw new CustomException("Session Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error updating session", exception);
            throw new CustomException("Failed to update session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   @Override
    public Session enrollSession(UserPrincipal userPrincipal, SessionDTO sessionDTO) {
        try {
            /* ============================================================
            1. Extract Session Time Information
            ------------------------------------------------------------
            - Get requested session start time from DTO
            - Calculate session duration (default 60 minutes)
            - Calculate session end time
            ============================================================ */
            Date sessionDate = sessionDTO.getSessionAt();

            int duration = sessionDTO.getDurationMinutes() != null
                    ? sessionDTO.getDurationMinutes()
                    : 60;

            Date endTime = new Date(sessionDate.getTime() + duration * 60 * 1000);

            /* ============================================================
            2. Validate Session Time
            ------------------------------------------------------------
            Business Rules:
            - Session cannot be scheduled in the past
            ============================================================ */
            LocalDateTime sessionTime = sessionDate
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            if (sessionTime.isBefore(LocalDateTime.now())) {
                throw new CustomException(
                        "Session time cannot be in the past",
                        HttpStatus.BAD_REQUEST
                );
            }

            /* ============================================================
            3. Retrieve or Create Student
            ------------------------------------------------------------
            - Try to find student by email
            - If student does not exist:
                • create new student
                • store Clerk user information
            ============================================================ */
            Student student = studentRepository.findByEmail(userPrincipal.getEmail())
                    .orElseGet(() -> {
                        Student newStudent = new Student();
                        newStudent.setStudentId(userPrincipal.getId());
                        newStudent.setEmail(userPrincipal.getEmail());
                        newStudent.setFirstName(userPrincipal.getFirstName());
                        newStudent.setLastName(userPrincipal.getLastName());
                        return studentRepository.save(newStudent);
            });

            /* ============================================================
            4. Retrieve Mentor and Subject
            ------------------------------------------------------------
            - Validate mentor exists
            - Validate subject exists
            ============================================================ */
            Mentor mentor = mentorRepository
                    .findByMentorId(String.valueOf(sessionDTO.getMentorId()))
                    .orElseThrow(() ->
                            new CustomException("Mentor not found", HttpStatus.NOT_FOUND));

            Subject subject = subjectRepository
                    .findById(sessionDTO.getSubjectId())
                    .orElseThrow(() ->
                            new CustomException("Subject not found", HttpStatus.NOT_FOUND));

            /* ============================================================
            5. Double Booking Validations
            ------------------------------------------------------------
            The system prevents the following scenarios:

            • Student booking same mentor at overlapping time
            • Student booking same subject at overlapping time
            • Mentor already having a session at that time
            ============================================================ */

            // Check if student already booked this mentor at same time
            boolean mentorConflict = sessionRepository
                    .existsByStudentAndMentorAndSessionAtBetween(
                            student, mentor, sessionDate, endTime
            );

            if (mentorConflict) {
                throw new CustomException(
                        "You already have a session with this mentor at this time",
                        HttpStatus.CONFLICT
                );
            }

            // Check if student already booked this subject at same time
            boolean subjectConflict = sessionRepository
                    .existsByStudentAndSubjectAndSessionAtBetween(
                            student, subject, sessionDate, endTime
            );

            if (subjectConflict) {
                throw new CustomException(
                        "You already booked this subject at this time",
                        HttpStatus.CONFLICT
                );
            }

            // Check if mentor already has another session at this time
            boolean mentorBusy = sessionRepository
                    .existsByMentorAndSessionAtBetween(
                            mentor, sessionDate, endTime
            );

            if (mentorBusy) {
                throw new CustomException(
                        "Mentor is not available for the selected time",
                        HttpStatus.CONFLICT
                );
            }

            /* ============================================================
            6. Create Session
            ------------------------------------------------------------
            - Build new session entity
            - Assign student, mentor, subject
            - Set default status values
            ============================================================ */

            Session session = Session.builder()
                    .student(student)
                    .mentor(mentor)
                    .subject(subject)
                    .sessionAt(sessionDate)
                    .durationMinutes(duration)
                    .sessionStatus(SessionStatus.PENDING)
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();

            Session savedSession = sessionRepository.save(session);


            /* ============================================================
            7. Update Mentor Statistics
            ------------------------------------------------------------
            - Increment total enrollment count
            ============================================================ */
            if (mentor.getTotalEnrollments() == null) {
                mentor.setTotalEnrollments(1);
            } else {
                mentor.setTotalEnrollments(mentor.getTotalEnrollments() + 1);
            }

            mentorRepository.save(mentor);


            /* ============================================================
            8. Return Created Session
            ============================================================ */
            return savedSession;
        } catch (CustomException exception) {
            log.error(
                    "Dependencies not found to map: {}, Failed to enroll session",
                    exception.getMessage()
            );
            throw exception;
        } catch (Exception exception) {
            log.error("Failed to enroll session", exception);
            throw new CustomException(
                    "Failed to enroll session",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public List<Session> getSessionsByStudentEmail(String studentEmail) {
        try {
            return sessionRepository.findByStudentEmail(studentEmail);
        } catch (Exception exception) {
            log.error("Failed to get sessions by student email: {}", studentEmail, exception);
            throw new CustomException("Failed to get sessions by student email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Session confirmPayment(Long sessionId) {
        try{
            Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));

            session.setPaymentStatus(PaymentStatus.CONFIRMED);
            return sessionRepository.save(session);
        } catch (Exception exception) {
            log.error("Failed to confirm payment with session id {}", sessionId, exception);
            throw new CustomException("Failed to confirm payment in session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Session markePaymentCompleted(Long sessionId) {
        try{
            Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));

            session.setPaymentStatus(PaymentStatus.COMPLETED);
            return sessionRepository.save(session);
        } catch (Exception exception) {
            log.error("Failed to complete payment with session id {}", sessionId, exception);
            throw new CustomException("Failed to complete payment in session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Session addMeetingLink(Long sessionId, String meetingLink) {
        try {
            Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));

            session.setMeetingLink(meetingLink);
            session.setSessionStatus(SessionStatus.SCHEDULED);

            return sessionRepository.save(session);
        } catch (Exception exception) {
            log.error("Failed to add meeting link session with id {}", sessionId, exception);
            throw new CustomException("Failed to add meeting link", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Session submitSessionReview(Long id, UserPrincipal userPrincipal, SessionReviewDTO reviewDTO) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));

        Student student = studentRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        if (!session.getStudent().getId().equals(student.getId())) {
            throw new CustomException("You are not allowed to review this session", HttpStatus.FORBIDDEN);
        }

        session.setStudentReview(reviewDTO.getStudentReview());
        session.setStudentRating(reviewDTO.getStudentRating());

        return sessionRepository.save(session);
    }

    @Override
    public void deleteSession(Long id) {
        try {
            sessionRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Failed to delete session with id {}", id, exception);
            throw new CustomException("Failed to delete session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }  
}
