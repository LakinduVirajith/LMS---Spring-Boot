package com.vpcodelabs.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vpcodelabs.lms.entities.Mentor;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    
}
