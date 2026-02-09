package com.vpcodelabs.lms.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpcodelabs.lms.dtos.MentorDTO;
import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.security.UserPrincipal;
import com.vpcodelabs.lms.services.MentorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/mentors")
@RequiredArgsConstructor
@Validated
public class MentorController extends AbstractController{
    private final MentorService mentorService;
    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
    public ResponseEntity<Mentor> createMentor(@Valid @RequestBody MentorDTO mentorDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Mentor mentor = modelMapper.map(mentorDTO, Mentor.class);
        mentor.setMentorId(userPrincipal.getId());
        mentor.setFirstName(userPrincipal.getFirstName());
        mentor.setLastName(userPrincipal.getLastName());
        mentor.setEmail(userPrincipal.getEmail());

        Mentor createdMentor = mentorService.createNewMentor(mentor);

        return sendCreatedResponse(createdMentor);
    }
    
    @GetMapping
    public ResponseEntity<Page<Mentor>> getAllMentors(Pageable pageable) {
        Page<Mentor> mentors = mentorService.getAllMentors(pageable);
        return sendOkResponse(mentors);
    }

    @GetMapping("{id}")
    public ResponseEntity<Mentor> getMentorById(@PathVariable Long id) {
        Mentor mentor = mentorService.getMentorById(id);
        return sendOkResponse(mentor);
    }    

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
    public ResponseEntity<Mentor> updateMentor(@PathVariable Long id, @Valid @RequestBody MentorDTO updatedMentorDTO) {
        Mentor mentor = modelMapper.map(updatedMentorDTO, Mentor.class);
        Mentor updatedMentor = mentorService.updateMentorById(id, mentor);
        return sendOkResponse(updatedMentor);

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
    public ResponseEntity<Mentor> deleteMentor(@PathVariable Long id) {
        mentorService.deleteMentor(id);
        return sendNoContentResponse();
    }
}
