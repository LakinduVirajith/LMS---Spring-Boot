package com.vpcodelabs.lms.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.entities.Subject;
import com.vpcodelabs.lms.repositories.MentorRepository;
import com.vpcodelabs.lms.repositories.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    public List<Subject> getAllSubjects(){
        return subjectRepository.findAll();
    }

    public Subject addNewSubject(Long mentorId, Subject subject){
        Mentor mentor = mentorRepository.findById(mentorId).get();
        subject.setMentor(mentor);
        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long id){
        return subjectRepository.findById(id).get();
    }

    public Subject updateSubjectById(Long id, Subject updatedSubject){
        Subject subject = subjectRepository.findById(id).get();
        modelMapper.map(updatedSubject, subject);
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id){
        subjectRepository.deleteById(id);
    }
}