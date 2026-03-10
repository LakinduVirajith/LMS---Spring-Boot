package com.vpcodelabs.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpcodelabs.lms.entities.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    List<Session> findByStudentEmail(String studentEmail);
}
