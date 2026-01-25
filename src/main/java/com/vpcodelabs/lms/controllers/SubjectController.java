package com.vpcodelabs.lms.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpcodelabs.lms.dtos.SubjectDTO;
import com.vpcodelabs.lms.entities.Subject;
import com.vpcodelabs.lms.services.SubjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
@Validated
public class SubjectController {
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @PostMapping
    public Subject createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        return subjectService.addNewSubject(subjectDTO.getMentorId(), subject);
    }

    @PutMapping("{id}")
    public Subject updateSubject(@PathVariable Long id, @RequestBody SubjectDTO updatedSubjectDTO) {
        Subject subject = modelMapper.map(updatedSubjectDTO, Subject.class);
        return subjectService.updateSubjectById(id, subject);
    }

    @DeleteMapping("{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }
}
