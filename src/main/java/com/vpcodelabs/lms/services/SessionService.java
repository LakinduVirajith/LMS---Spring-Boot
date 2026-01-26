package com.vpcodelabs.lms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vpcodelabs.lms.dtos.SessionDTO;
import com.vpcodelabs.lms.entities.Session;

public interface SessionService {
    Session createNewSession(SessionDTO sessionDTO);

    Page<Session> getAllSessions(Pageable pageable);
    
    Session getSessionById(Long id);
    
    Session updateSessionById(Long id, SessionDTO updatedSessionDTO);
    
    void deleteSession(Long id);
}
