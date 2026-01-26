package com.vpcodelabs.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpcodelabs.lms.entities.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
}
