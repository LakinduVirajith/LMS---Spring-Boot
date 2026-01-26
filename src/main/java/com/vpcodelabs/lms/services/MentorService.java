package com.vpcodelabs.lms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vpcodelabs.lms.entities.Mentor;

public interface MentorService {
    Mentor createNewMentor(Mentor mentor);

    Page<Mentor> getAllMentors(Pageable pageable);
    
    Mentor getMentorById(Long id);
    
    Mentor updateMentorById(Long id, Mentor updatedMentor);
    
    void deleteMentor(Long id);
}