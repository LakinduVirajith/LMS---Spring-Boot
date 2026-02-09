package com.vpcodelabs.lms.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.entities.Subject;
import com.vpcodelabs.lms.exceptions.CustomException;
import com.vpcodelabs.lms.repositories.MentorRepository;
import com.vpcodelabs.lms.repositories.SubjectRepository;
import com.vpcodelabs.lms.services.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    public Subject addNewSubject(Long mentorId, Subject subject){
        try {
            Mentor mentor = mentorRepository.findById(mentorId).get();
            subject.setMentor(mentor);
            return subjectRepository.save(subject);
        } catch (Exception exception) {
            log.error("Failed to create new subject", exception);
            throw new CustomException("Failed to create new subject", HttpStatus.CONFLICT);
        }
    }

    public Page<Subject> getAllSubjects(Pageable pageable){
         try {
            log.debug("getting subjects");
            return subjectRepository.findAll(pageable);
        } catch (Exception exception) {
            log.error("Failed to get all subjects", exception);
            throw new CustomException("Failed to get all subjects", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Subject getSubjectById(Long id){
        try {
            Subject subject = subjectRepository.findById(id).orElseThrow(
                    () -> new CustomException("Subject Not found", HttpStatus.NOT_FOUND)
            );
            log.info("Successfully fetched subject {}", id);
            return subject;
        } catch (CustomException exception) {
            log.warn("Subject not found with id: {} to fetch", id, exception);
            throw new CustomException("Subject Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error getting subject", exception);
            throw new CustomException("Failed to get subject", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Subject updateSubjectById(Long id, Subject updatedSubject){
        try {
            Subject subject = subjectRepository.findById(id).orElseThrow(
                    () -> new CustomException("Subject Not found", HttpStatus.NOT_FOUND)
            );
            modelMapper.map(updatedSubject, subject);
            return subjectRepository.save(subject);
        } catch (CustomException exception) {
            log.warn("Subject not found with id: {} to update", id, exception);
            throw new CustomException("Subject Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error updating subject", exception);
            throw new CustomException("Failed to update subject", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteSubject(Long id){
        try {
            subjectRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Failed to delete subject with id {}", id, exception);
            throw new CustomException("Failed to delete subject", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
