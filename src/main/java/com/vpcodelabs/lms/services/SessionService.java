package com.vpcodelabs.lms.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vpcodelabs.lms.dtos.SessionDTO;
import com.vpcodelabs.lms.entities.Session;
import com.vpcodelabs.lms.security.UserPrincipal;

public interface SessionService {
    Session createNewSession(SessionDTO sessionDTO);

    Page<Session> getAllSessions(Pageable pageable);
    
    Session getSessionById(Long id);
    
    Session updateSessionById(Long id, SessionDTO updatedSessionDTO);

    Session enrollSession(UserPrincipal userPrincipal, SessionDTO sessionDTO);

    List<Session> getSessionsByStudentEmail(String studentEmail);
    
    void deleteSession(Long id);
}
