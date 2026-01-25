package com.vpcodelabs.lms.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.exception.CustomException;
import com.vpcodelabs.lms.repositories.MentorRepository;
import com.vpcodelabs.lms.services.MentorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    public Mentor createNewMentor(Mentor mentor) {
        try {
            return mentorRepository.save(mentor);
        } catch (Exception exception) {
            // What, When, Where, Why
            log.error("Failed to create new mentor", exception);
            throw new CustomException("Failed to create new mentor", HttpStatus.CONFLICT);
        }
    }

    public Page<Mentor> getAllMentors(Pageable pageable) {
        try {
            log.debug("getting mentors");
            return mentorRepository.findAll(pageable);
        } catch (Exception exception) {
            log.error("Failed to get all mentors", exception);
            throw new CustomException("Failed to get all mentors", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public Mentor getMentorById(Long id) {
        try {
            Mentor mentor = mentorRepository.findById(id).orElseThrow(
                    () -> new CustomException("Mentor Not found", HttpStatus.NOT_FOUND)
            );
            log.info("Successfully fetched mentor {}", id);
            return mentor;
        } catch (CustomException exception) {
            log.warn("Mentor not found with id: {} to fetch", id, exception);
            throw new CustomException("Mentor Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error getting mentor", exception);
            throw new CustomException("Failed to get mentor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Mentor updateMentorById(Long id, Mentor updatedMentor) {
        try {
            Mentor mentor = mentorRepository.findById(id).orElseThrow(
                    () -> new CustomException("Mentor Not found", HttpStatus.NOT_FOUND)
            );
            modelMapper.map(updatedMentor, mentor);
            return mentorRepository.save(mentor);
        } catch (CustomException exception) {
            log.warn("Mentor not found with id: {} to update", id, exception);
            throw new CustomException("Mentor Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error updating mentor", exception);
            throw new CustomException("Failed to update mentor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteMentor(Long id) {
        try {
            mentorRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Failed to delete mentor with id {}", id, exception);
            throw new CustomException("Failed to delete mentor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
