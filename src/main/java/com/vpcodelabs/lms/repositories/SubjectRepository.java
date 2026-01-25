package com.vpcodelabs.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vpcodelabs.lms.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
}
