package com.vpcodelabs.lms.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vpcodelabs.lms.dtos.SessionDTO;
import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.entities.Session;
import com.vpcodelabs.lms.entities.Student;
import com.vpcodelabs.lms.entities.Subject;
import com.vpcodelabs.lms.exception.CustomException;
import com.vpcodelabs.lms.repositories.MentorRepository;
import com.vpcodelabs.lms.repositories.SessionRepository;
import com.vpcodelabs.lms.repositories.StudentRepository;
import com.vpcodelabs.lms.repositories.SubjectRepository;
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

    public Page<Session> getAllSessions(Pageable pageable) {
        try {
            log.debug("getting sessions");
            return sessionRepository.findAll(pageable);
        } catch (Exception exception) {
            log.error("Failed to get all sessions", exception);
            throw new CustomException("Failed to get all sessions", HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

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

    public void deleteSession(Long id) {
        try {
            sessionRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Failed to delete session with id {}", id, exception);
            throw new CustomException("Failed to delete session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
