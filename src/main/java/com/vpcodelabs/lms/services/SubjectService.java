package com.vpcodelabs.lms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vpcodelabs.lms.entities.Subject;
public interface SubjectService {
    Subject addNewSubject(Long mentorId, Subject subject);
    
    Page<Subject> getAllSubjects(Pageable pageable);
    
    Subject getSubjectById(Long id);
    
    Subject updateSubjectById(Long id, Subject updatedSubject);
    
    void deleteSubject(Long id);
}