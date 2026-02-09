package com.vpcodelabs.lms.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("isAuthenticated()")
public class SubjectController extends AbstractController {
    private final ModelMapper modelMapper;
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Subject createdSubject =  subjectService.addNewSubject(subjectDTO.getMentorId(), subject);
        return sendCreatedResponse(createdSubject);
    }

    @GetMapping
    public ResponseEntity<Page<Subject>> getAllSubjects(Pageable pageable) {
        Page<Subject> subjects = subjectService.getAllSubjects(pageable);
        return sendOkResponse(subjects);
    }

    @GetMapping("{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id);
        return sendOkResponse(subject);
    }

    @PutMapping("{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectDTO updatedSubjectDTO) {
        Subject subject = modelMapper.map(updatedSubjectDTO, Subject.class);
        Subject updatedSubject = subjectService.updateSubjectById(id, subject);
        return sendOkResponse(updatedSubject);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return sendNoContentResponse();
    }
}
