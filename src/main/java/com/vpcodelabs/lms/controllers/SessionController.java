package com.vpcodelabs.lms.controllers;

import static com.vpcodelabs.lms.constants.UserRoles.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpcodelabs.lms.dtos.SessionDTO;
import com.vpcodelabs.lms.dtos.SessionResponseDTO;
import com.vpcodelabs.lms.entities.Session;
import com.vpcodelabs.lms.security.UserPrincipal;
import com.vpcodelabs.lms.services.SessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/sessions")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class SessionController extends AbstractController{

    private final SessionService sessionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "', '" + ROLE_STUDENT + "')")
    public ResponseEntity<Session> createSession(@Valid @RequestBody SessionDTO sessionDTO) {
        Session createdSession = sessionService.createNewSession(sessionDTO);

        return sendCreatedResponse(createdSession);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<Page<SessionResponseDTO>> getAllSessions(Pageable pageable) {
        Page<SessionResponseDTO> sessions = sessionService.getAllSessions(pageable);
         return sendOkResponse(sessions);
    }

    @GetMapping("{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id);
        return sendOkResponse(session);
    }

    @PutMapping("{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @Valid @RequestBody SessionDTO updatedSessionDTO) {
        Session updatedSession = sessionService.updateSessionById(id, updatedSessionDTO);
        return sendOkResponse(updatedSession);
    }

    @PostMapping("/enrollment")
    @PreAuthorize("hasAnyRole('" + ROLE_STUDENT + "')")
    public ResponseEntity<SessionResponseDTO> postEnrollment(@RequestBody SessionDTO sessionDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Session enrolledSession = sessionService.enrollSession(userPrincipal, sessionDTO);
        
        return sendCreatedResponse(convertToSessionResponseDTO(enrolledSession));
    }

    @GetMapping("/enrollments")
    @PreAuthorize("hasAnyRole('" + ROLE_STUDENT + "')")
    public ResponseEntity<List<SessionResponseDTO>> getEnrollments(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Session> sessions = sessionService.getSessionsByStudentEmail(userPrincipal.getEmail());
        List<SessionResponseDTO> enrollments = sessions.stream()
            .map(this::convertToSessionResponseDTO)
            .collect(Collectors.toList());
        return sendOkResponse(enrollments);
    }

    @PatchMapping("{id}/confirm-payment")
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<SessionResponseDTO> confirmPayment(@PathVariable Long id) {
        Session session = sessionService.confirmPayment(id);
        return sendOkResponse(convertToSessionResponseDTO(session));
    }

    @PatchMapping("{id}/complete")
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<SessionResponseDTO> markCompleted(@PathVariable Long id) {
        Session session = sessionService.markePaymentCompleted(id);
        return sendOkResponse(convertToSessionResponseDTO(session));
    }

    @PatchMapping("{id}/meeting-link")
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "', '" + ROLE_MENTOR + "')")
    public ResponseEntity<SessionResponseDTO> addMeetingLink(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String link = body.get("link");
        Session session = sessionService.addMeetingLink(id, link);
        return sendOkResponse(convertToSessionResponseDTO(session));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Session> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return sendNoContentResponse();
    }

    

    private SessionResponseDTO convertToSessionResponseDTO(Session session) {
        SessionResponseDTO sessionResponseDTO = SessionResponseDTO.builder()
            .id(session.getId())
            .mentorName(session.getMentor().getFirstName() + " " + session.getMentor().getLastName())
            .mentorProfileImageUrl(session.getMentor().getProfileImageUrl())
            .subjectName(session.getSubject().getSubjectName())
            .sessionAt(session.getSessionAt())
            .durationMinutes(session.getDurationMinutes())
            .sessionStatus(session.getSessionStatus().name())
            .paymentStatus(session.getPaymentStatus().name())
            .meetingLink(session.getMeetingLink())
            .build();
        return sessionResponseDTO;
    }
}
