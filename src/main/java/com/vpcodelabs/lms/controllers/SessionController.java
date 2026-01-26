package com.vpcodelabs.lms.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpcodelabs.lms.dtos.SessionDTO;
import com.vpcodelabs.lms.entities.Session;
import com.vpcodelabs.lms.services.SessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/sessions")
@RequiredArgsConstructor
@Validated
public class SessionController extends AbstractController{

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<Session> createSession(@Valid @RequestBody SessionDTO sessionDTO) {
        Session createdSession = sessionService.createNewSession(sessionDTO);

        return sendCreatedResponse(createdSession);
    }

    @GetMapping
    public ResponseEntity<Page<Session>> getAllSessions(Pageable pageable) {
        Page<Session> sessions = sessionService.getAllSessions(pageable);
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

    @DeleteMapping("{id}")
    public ResponseEntity<Session> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return sendNoContentResponse();
    }
}
